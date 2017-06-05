package sk.fri.uniza.sign;

import eu.europa.esig.dss.*;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import sk.fri.uniza.PostingSheet;
import sk.fri.uniza.SignRequest;
import sk.fri.uniza.SignaturePolicy;
import sk.fri.uniza.Signer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.UnrecoverableKeyException;
import java.util.*;

public class DSSSignerAdapter implements Signer {

    private Collection<SignaturePolicy> signaturePolicies;
    private Collection<SignatureStructure> supportedStructures;
    private Collection<DigestAlgorithm> supportedDigestAlgorithms;
    private Collection<SignatureLevel> supportedLevels;


    private Map<SignatureStructure, SignaturePackaging> structureIntoDSSMap;
    private Map<SignatureLevel, eu.europa.esig.dss.SignatureLevel> levelIntoDssMap;
    private Map<DigestAlgorithm, eu.europa.esig.dss.DigestAlgorithm> digestAlgIntoDSSMap;
    private DSSXAdESSigner dssXAdESSigner;


    public DSSSignerAdapter(DSSXAdESSigner dssXAdESSigner) {
        this.dssXAdESSigner = dssXAdESSigner;
        supportedStructures = createCollectionOfSupportedStructures();
        supportedDigestAlgorithms = createCollectionOfSupportedDigestAlgorithms();
        supportedLevels = createCollectionOfSupportedLevels();

        structureIntoDSSMap = createStructureIntoDSSMap();
        levelIntoDssMap = createLevelMappingIntoDSS();
        digestAlgIntoDSSMap = createDigestAlgIntoDSSMap();
    }

    private Collection<SignatureStructure> createCollectionOfSupportedStructures() {
        Set<SignatureStructure> s = new HashSet<>();
        s.add(SignatureStructure.ENVELOPED);
        return s;
    }

    private Collection<DigestAlgorithm> createCollectionOfSupportedDigestAlgorithms() {
        Set<DigestAlgorithm> s = new HashSet<>();
        s.add(DigestAlgorithm.SHA1);
        return s;
    }

    private Collection<SignatureLevel> createCollectionOfSupportedLevels() {
        Set<SignatureLevel> s = new HashSet<>();
        s.add(SignatureLevel.B);
        return s;
    }

    private Map<SignatureLevel, eu.europa.esig.dss.SignatureLevel> createLevelMappingIntoDSS() {
        Map<SignatureLevel, eu.europa.esig.dss.SignatureLevel> m = new HashMap<>();
        m.put(SignatureLevel.B, eu.europa.esig.dss.SignatureLevel.XAdES_BASELINE_B);
        m.put(SignatureLevel.T, eu.europa.esig.dss.SignatureLevel.XAdES_BASELINE_T);
        return m;
    }

    private Map<SignatureStructure, SignaturePackaging> createStructureIntoDSSMap() {
        Map<SignatureStructure, SignaturePackaging> m = new HashMap<>();
        m.put(SignatureStructure.ENVELOPED, SignaturePackaging.ENVELOPED);
        m.put(SignatureStructure.ENVELOPING, SignaturePackaging.ENVELOPING);
        return m;
    }

    private Map<DigestAlgorithm, eu.europa.esig.dss.DigestAlgorithm> createDigestAlgIntoDSSMap() {
        Map<DigestAlgorithm, eu.europa.esig.dss.DigestAlgorithm> m = new HashMap<>();
        m.put(DigestAlgorithm.SHA1, eu.europa.esig.dss.DigestAlgorithm.SHA1);
        m.put(DigestAlgorithm.SHA256, eu.europa.esig.dss.DigestAlgorithm.SHA256);
        return m;
    }

    @Override
    public Collection<DigestAlgorithm> getDigestAlgorithms(SignaturePolicy policy) {
        return supportedDigestAlgorithms;
    }

    @Override
    public Collection<SignatureStructure> getSignatureStructures(SignaturePolicy policy) {
        return supportedStructures;
    }

    @Override
    public Collection<SignatureLevel> getSignatureLevels(SignaturePolicy policy) {
        return supportedLevels;
    }

    @Override
    public Collection<SignaturePolicy> getSignaturePolicies() {
        if (signaturePolicies == null)
            throw new InitException("Signer should provide at least on signature policy");
        return signaturePolicies;
    }

    public void setSignaturePolicies(Collection<SignaturePolicy> signaturePolicies) {
        this.signaturePolicies = signaturePolicies;
    }

    @Override
    public PostingSheet signPostingSheet(SignRequest signRequest) {
        try {
            return tryDSSsign(signRequest);
        } catch (DSSException e) {
            if (keyStoreAuthenticationFailed(e))
                throw new KeyStoreAuthenticationException(e);

            throw new UnsupportedOperationException();
        }
    }

    private boolean keyStoreAuthenticationFailed(DSSException e) {
        return (e.getCause() instanceof IOException) &&
                (e.getCause().getCause() instanceof UnrecoverableKeyException);
    }

    private PostingSheet tryDSSsign(SignRequest signRequest) {
        DSSDocument toSignDoc = adaptPostingSheet(signRequest);
        SignatureTokenConnection signingToken = adaptSignatureToken(signRequest);
        XAdESSignatureParameters params = adaptSignatureParameters(signRequest);
        DSSDocument signedDoc = dssXAdESSigner.sign(params, toSignDoc, signingToken);
        PostingSheet postingSheet = recordSignedContent(signRequest, signedDoc);
        return postingSheet;
    }

    private DSSDocument adaptPostingSheet(SignRequest signRequest) {
        InMemoryDocument inMemoryDocument = new InMemoryDocument();
        PostingSheet postingSheet = signRequest.getPostingSheet();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        postingSheet.writeOriginalContentTo(baos);
        inMemoryDocument.setBytes(baos.toByteArray());
        inMemoryDocument.setMimeType(MimeType.XML); //TODO change
        return inMemoryDocument;
    }

    private Pkcs12SignatureToken adaptSignatureToken(SignRequest signRequest) {
        return new Pkcs12SignatureToken(signRequest.getPasswordToP12(),
                signRequest.getFilePathToP12());
    }

    private XAdESSignatureParameters adaptSignatureParameters(SignRequest signRequest) {
        XAdESSignatureParameters parameters = new XAdESSignatureParameters();
        parameters.setSignaturePackaging(adaptStructure(signRequest.getSignatureStructure()));
        parameters.setSignatureLevel(adaptLevel(signRequest.getSignatureLevel()));
        parameters.setDigestAlgorithm(adaptDigestAlg(signRequest.getDigestAlgorithm()));
        if (signRequest.getSigningDate() != null) {
            parameters.bLevel().setSigningDate(signRequest.getSigningDate());
        }
        parameters.bLevel().setSignaturePolicy(adaptPolicy(signRequest.getSignaturePolicy()));
        return parameters;
    }

    public eu.europa.esig.dss.SignatureLevel adaptLevel(SignatureLevel level) {
        eu.europa.esig.dss.SignatureLevel l = levelIntoDssMap.get(level);
        if (l == null)
            throw new NotAdaptableLevel("It tried adapt " + level);

        return l;
    }

    public SignaturePackaging adaptStructure(SignatureStructure structure) {
        SignaturePackaging p = structureIntoDSSMap.get(structure);
        if (p == null)
            throw new NotAdaptableStructure("It tried adapt " + structure);

        return p;
    }


    public eu.europa.esig.dss.DigestAlgorithm adaptDigestAlg(DigestAlgorithm alg) {
        eu.europa.esig.dss.DigestAlgorithm a = digestAlgIntoDSSMap.get(alg);
        if (a == null)
            throw new NotAdaptableDigestAlg("It tried adapt " + alg);
        return a;
    }

    public Policy adaptPolicy(SignaturePolicy policy) {
        Policy p = new Policy();
        p.setId(policy.getOID());
        p.setDigestAlgorithm(adaptDigestAlg(policy.getDigestAlgorithm()));
        p.setDigestValue(policy.getDigestValue());
        return p;
    }

    private PostingSheet recordSignedContent(SignRequest signRequest, DSSDocument signedDocument) {
        PostingSheet postingSheet = signRequest.getPostingSheet();
        postingSheet.readSignedContentFrom(signedDocument.openStream());
        return postingSheet;
    }

    static class NotAdaptableStructure extends RuntimeException {
        NotAdaptableStructure(String s) {
            super(s);
        }
    }

    static class NotAdaptableLevel extends RuntimeException {
        NotAdaptableLevel(String s) {
            super(s);
        }
    }

    static class NotAdaptableDigestAlg extends RuntimeException {
        NotAdaptableDigestAlg(String s) {
            super(s);
        }
    }

}
