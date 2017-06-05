package sk.fri.uniza.sign;

import eu.europa.esig.dss.Policy;
import eu.europa.esig.dss.SignaturePackaging;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sk.fri.uniza.*;
import sk.fri.uniza.Signer.DigestAlgorithm;
import sk.fri.uniza.Signer.SignatureLevel;
import sk.fri.uniza.Signer.SignatureStructure;
import sk.fri.uniza.mocks.*;
import sk.fri.uniza.testUtils.TestResourceUtil;
import sk.fri.uniza.utils.DateUtil;
import sk.fri.uniza.utils.UserMessage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static sk.fri.uniza.testUtils.TestResourceUtil.VALID_KEYSTORE_FILEPATH;
import static sk.fri.uniza.testUtils.TestResourceUtil.relativeResource2AbsolutePath;

public class DSSSignerAdapterTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final String XML_EXAMPLE = relativeResource2AbsolutePath("xml_example.xml");
    private final String XML_EXAMPLE_SIGNED = relativeResource2AbsolutePath("xml_example-b-enveloped-sha1-20170329_133929-withPolicy.xml");

    private DSSSignerAdapter signerAdapter;

    @Before
    public void setUp() throws Exception {
        signerAdapter = new DSSSignerAdapter(new DSSSignerStub());
    }

    /* Sign Structure */
    @Test
    public void signerShouldProvideFollowingListOfStructures() throws Exception {
        Collection<SignatureStructure> structures = signerAdapter.getSignatureStructures(null);
        assertThat(structures, notNullValue());
        assertThat(structures.isEmpty(), is(false));
        assertThat(structures.contains(SignatureStructure.ENVELOPED), is(true));
    }

    @Test
    public void signerShouldNotProvideFollowingListOfStructures() throws Exception {
        Collection<SignatureStructure> structures = signerAdapter.getSignatureStructures(null);
        assertThat(structures.contains(SignatureStructure.NONE), is(false));
        assertThat(structures.contains(SignatureStructure.ENVELOPING), is(false));
    }

    @Test
    public void getSignatureStructureShouldBeIdempotent() throws Exception {
        Collection<SignatureStructure> signatureStructs = signerAdapter.getSignatureStructures(null);
        for (int i = 0; i < 5 ; i++ ){
            assertThat(signatureStructs, equalTo(signerAdapter.getSignatureStructures(null)));
        }
    }

    @Test
    public void adaptStructureShouldFail_WhenAdaptionNotExists() throws Exception {
        expectedException.expect(DSSSignerAdapter.NotAdaptableStructure.class);
        expectedException.expectMessage("It tried adapt " + SignatureStructure.NONE);
        signerAdapter.adaptStructure(SignatureStructure.NONE);
    }

    @Test
    public void shouldAdaptStruct_ENVELOPED_INTO_DSS_ENVELOPED() throws Exception {
        SignaturePackaging packaging = signerAdapter.adaptStructure(SignatureStructure.ENVELOPED);
        assertThat(packaging, is(SignaturePackaging.ENVELOPED));
    }

    @Test
    public void shouldAdaptStruct_ENVELOPING_INTO_DSS_ENVELOPING() throws Exception {
        SignaturePackaging structure = signerAdapter.adaptStructure(SignatureStructure.ENVELOPING);
        assertThat(structure, is(SignaturePackaging.ENVELOPING));
    }

    /* Sign Level */

    @Test
    public void signerShouldProvideFollowingListOfLevels() throws Exception {
        Collection<SignatureLevel> levels = signerAdapter.getSignatureLevels(null);
        assertThat(levels, notNullValue());
        assertThat(levels.isEmpty(), is(false));
        assertThat(levels.contains(SignatureLevel.B), is(true));
    }

    @Test
    public void signerShouldNotProvideFollowingListOfLevels() throws Exception {
        Collection<SignatureLevel> levels = signerAdapter.getSignatureLevels(null);
        assertThat(levels.contains(SignatureLevel.T), is(false));
        assertThat(levels.contains(SignatureLevel.NONE), is(false));
    }

    @Test
    public void getSignatureLevelsShouldBeIdempotent() throws Exception {
        Collection<SignatureLevel> signatureLevels = signerAdapter.getSignatureLevels(null);
        for (int i = 0; i < 5 ; i++) {
            assertThat(signatureLevels, equalTo(signerAdapter.getSignatureLevels(null)));
        }
    }

    @Test
    public void adaptLevelShouldFail_WhenAdaptionNotExists() throws Exception {
        expectedException.expect(DSSSignerAdapter.NotAdaptableLevel.class);
        expectedException.expectMessage("It tried adapt " + SignatureLevel.NONE);
        signerAdapter.adaptLevel(SignatureLevel.NONE);
    }

    @Test
    public void shouldAdaptSupportedLevel_B_into_XAdES_BASELINE_B() throws Exception {
        eu.europa.esig.dss.SignatureLevel dssLevel = signerAdapter.adaptLevel(SignatureLevel.B);
        assertThat(dssLevel, is(eu.europa.esig.dss.SignatureLevel.XAdES_BASELINE_B));
    }

    @Test
    public void shouldAdaptSupportedLevel_T_intoXAdES_BASELINE_T() throws Exception {
        eu.europa.esig.dss.SignatureLevel dssLevel = signerAdapter.adaptLevel(SignatureLevel.T);
        assertThat(dssLevel, is(eu.europa.esig.dss.SignatureLevel.XAdES_BASELINE_T));
    }

    /* Sign Digest Algorithm */

    @Test
    public void signerShouldProvideFollowingListOfDigestAlgorithms() throws Exception {
        Collection<DigestAlgorithm> digestAlgorithms = signerAdapter.getDigestAlgorithms(null);
        assertThat(digestAlgorithms, notNullValue());
        assertThat(digestAlgorithms.isEmpty(), is(false));
        assertThat(digestAlgorithms.contains(DigestAlgorithm.SHA1), is(true));
    }

    @Test
    public void signerShouldNotProvideFollowingListOfDigestAlgorithms() throws Exception {
        Collection<DigestAlgorithm> digestAlgorithms = signerAdapter.getDigestAlgorithms(null);
        assertThat(digestAlgorithms.contains(DigestAlgorithm.NONE), is(false));
        assertThat(digestAlgorithms.contains(DigestAlgorithm.SHA256), is(false));
    }

    @Test
    public void getDigestAlgorithmShouldBeIdempotent() throws Exception {
        Collection<DigestAlgorithm> digestAlgorithms = signerAdapter.getDigestAlgorithms(null);
        for (int i = 0; i < 5 ; i++ ){
            assertThat(digestAlgorithms, equalTo(signerAdapter.getDigestAlgorithms(null)));
        }
    }

    @Test
    public void adaptDigestAlgShouldFail_WhenAdaptionNotExists() throws Exception {
        expectedException.expect(DSSSignerAdapter.NotAdaptableDigestAlg.class);
        expectedException.expectMessage("It tried adapt " + DigestAlgorithm.NONE);
        signerAdapter.adaptDigestAlg(DigestAlgorithm.NONE);
    }

    @Test
    public void shouldAdaptDigest_SHA1_intoDSS_SHA1() throws Exception {
        eu.europa.esig.dss.DigestAlgorithm alg = signerAdapter.adaptDigestAlg(DigestAlgorithm.SHA1);
        assertThat(alg, is(eu.europa.esig.dss.DigestAlgorithm.SHA1));
    }

    /*SignaturePolicy tests*/
    @Test
    public void getPoliciesShouldFailedWhenPoliciesWasNotInit() throws Exception {
        expectedException.expect(Signer.InitException.class);
        expectedException.expectMessage("Signer should provide at least on signature policy");
        signerAdapter.getSignaturePolicies();
    }

    @Test
    public void signerShouldProvideAtLeastOneSignaturePolicy() throws Exception {
        SignaturePolicyStub policy = new SignaturePolicyStub();
        signerAdapter.setSignaturePolicies(Arrays.asList(new SignaturePolicyStub[]{policy}));

        Collection<SignaturePolicy> policies = signerAdapter.getSignaturePolicies();
        assertThat(policies, is(not(nullValue())));
        assertThat(policies.isEmpty(), is(false));
    }

    @Test
    public void getSignaturePolicyShouldBeIdempotent() throws Exception {
        signerAdapter.setSignaturePolicies(Collections.singleton(new SignaturePolicyDummy()));
        Collection<SignaturePolicy> signaturePolicies = signerAdapter.getSignaturePolicies();
        for (int i = 0; i < 5 ; i++ ){
            assertThat(signaturePolicies, equalTo(signerAdapter.getSignaturePolicies()));
        }
    }

    @Test
    public void shouldAdaptSignaturePolicy_IntoDSSPolicy() throws Exception {
        SignaturePolicy policy = new SignaturePolicyStub();
        Policy dssPolicy = signerAdapter.adaptPolicy(policy);
        assertThat(dssPolicy.getId(), CoreMatchers.is(TestResourceUtil.POLICY_OID));
        assertThat(dssPolicy.getDigestAlgorithm(), is(eu.europa.esig.dss.DigestAlgorithm.SHA1));
        assertThat(dssPolicy.getDigestValue(), is(TestResourceUtil.POLICY_DIGEST));
    }

    @Test
    public void signShouldFailed_WhenWrongPasswordOrP12FileProvided() throws Exception {
        expectedException.expect(Signer.KeyStoreAuthenticationException.class);
        expectedException.expectMessage(UserMessage.AUTHENTICATION_KEYSTORE_FAILED);
        DSSSignerAdapter signer = new DSSSignerAdapter(new FailP12AuthenticationDSSSignerStub());
        signer.signPostingSheet(createAndSetSignRequestStub("wrong password".toCharArray()));
    }

    private SignRequestStub createAndSetSignRequestStub(char[] password) {
        SignRequestStub request = new SignRequestStub();
        request.setPostingSheet(new PostingSheetStub());
        request.setPassword(password);
        request.setFilePathToP12(VALID_KEYSTORE_FILEPATH);

        return request;
    }

    /* integration tests with ETSI Checker results */
    @Test
    public void signShouldSucceed_B_Enveloped_SHA1() throws Exception {
        signerAdapter = new DSSSignerAdapter(new DSSXAdESSigner());
        XMLPostingSheet postingSheetStub = createPostingSheetFromFile(XML_EXAMPLE);
        SignRequest sr = createSignRequest(postingSheetStub);
        PostingSheet ps = signerAdapter.signPostingSheet(sr);
        assertThat(getSignedContent(ps), is(readContentVerifiedByETSIChecker(XML_EXAMPLE_SIGNED)));
    }

    private String getSignedContent(PostingSheet ps) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ps.writeSignedContentTo(baos);
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    private XMLPostingSheet createPostingSheetFromFile(String filePath) throws FileNotFoundException {
        XMLPostingSheet postingSheetStub = new XMLPostingSheet();
        postingSheetStub.readOriginalContentFrom(new FileInputStream(filePath));
        return postingSheetStub;
    }

    private SignRequest createSignRequest(XMLPostingSheet postingSheetStub) {
        SignRequest sr = new SignRequest();
        sr.setPostingSheet(postingSheetStub);
        sr.setSignaturePolicy(createPolicyStub());
        sr.setSignatureLevel(SignatureLevel.B);
        sr.setSignatureStructure(SignatureStructure.ENVELOPED);
        sr.setDigestAlgorithm(DigestAlgorithm.SHA1);
        sr.setFilePathToP12(VALID_KEYSTORE_FILEPATH);
        sr.setPasswordToP12("password".toCharArray());
        sr.setSigningDate(DateUtil.parseTimeStamp("2017-03-29 13:39:29"));
        return sr;
    }

    private SignaturePolicy createPolicyStub() {
        SignaturePolicyStub p = new SignaturePolicyStub();
        p.setOID("1.3.158.36061701.1.2.2");
        p.setDigestAlgorithm(DigestAlgorithm.SHA256);
        p.setDigestValue("C876D4CA4A875295C838244A40B422CC9ECA3BB397E0E0E6CB8D6242F37F8A9F".getBytes());
        return p;
    }

    private String readContentVerifiedByETSIChecker(String path) throws IOException {
        String signedContent = "";
        BufferedReader br = new BufferedReader(new FileReader(path));
        String s;
        while ((s = br.readLine()) != null){
            signedContent += s;
        }
        return  signedContent;
    }

}
