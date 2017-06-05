package sk.fri.uniza;

import eu.europa.esig.dss.*;
import eu.europa.esig.dss.token.AbstractSignatureTokenConnection;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.xades.DSSReference;
import eu.europa.esig.dss.xades.DSSTransform;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DssTest
{

    DSSDocument toSignDocument;

    SignatureTokenConnection signingToken;
    DSSPrivateKeyEntry privateKey;

    @Test
    public void signSimpleHelloWorldXML() throws Exception {

        // GET document to be signed -
        // Return DSSDocument toSignDocument
        prepareXmlDoc();

        // Get a token connection based on a pkcs12 file commonly used to store private
        // keys with accompanying public key certificates, protected with a password-based
        // symmetric key -
        // Return AbstractSignatureTokenConnection signingToken
        // and it's first private key entry from the PKCS12 store
        // Return DSSPrivateKeyEntry privateKey
        preparePKCS12TokenAndKey();

        // Preparing parameters for the XAdES signature
        XAdESSignatureParameters parameters = new XAdESSignatureParameters();
        parameters.setSignatureLevel(SignatureLevel.XAdES_BASELINE_B);
        parameters.setSignaturePackaging(SignaturePackaging.ENVELOPED);
        // We set the digest algorithm to use with the signature algorithm. You must use the
        // same parameter when you invoke the method sign on the token. The default value is SHA256
        parameters.setDigestAlgorithm(DigestAlgorithm.SHA1);

        parameters.bLevel().setSigningDate(parseDate("2017-03-29 13:39:29"));

        Policy policy = new Policy();
        policy.setId("1.3.158.36061701.1.2.2");
        policy.setDigestAlgorithm(DigestAlgorithm.SHA256);
        policy.setDigestValue("C876D4CA4A875295C838244A40B422CC9ECA3BB397E0E0E6CB8D6242F37F8A9F".getBytes());

        parameters.bLevel().setSignaturePolicy(policy);

        // We set the signing certificate
        parameters.setSigningCertificate(privateKey.getCertificate());
        // We set the certificate chain
        parameters.setCertificateChain(privateKey.getCertificateChain());
        //from private key will be encrypt alg detected and used


        // Create common certificate verifier
        CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();

        // Create XAdES service for signature
        XAdESService service = new XAdESService(commonCertificateVerifier);

        // Get the SignedInfo XML segment that need to be signed.
        ToBeSigned dataToSign = service.getDataToSign(toSignDocument, parameters);

        // This function obtains the signature value for signed information using the
        // private key and specified algorithm
        SignatureValue signatureValue = signingToken.sign(dataToSign, parameters.getDigestAlgorithm(), privateKey);

        // We invoke the service to sign the document with the signature value obtained in
        // the previous step.
        DSSDocument signedDocument = service.signDocument(toSignDocument, parameters, signatureValue);

         try {
             signedDocument.save("src/test/resources/eph-signed.xml");
         } catch (IOException e) {
             e.printStackTrace();
         }

    }

    private Date parseDate(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            throw new IllegalStateException();
        }
    }

    private void prepareXmlDoc() {
        String toSignFilePath = getPathFromResource("/xml_example.xml");
        toSignDocument = new FileDocument(toSignFilePath);
    }

    private String getPathFromResource(String s) {
        URL uri = getClass().getResource(s);
        return uri.getPath();
    }

    private void preparePKCS12TokenAndKey() throws IOException {
        String pkcs12TokenFile = getPathFromResource("/user_a_rsa.p12");
        signingToken = new Pkcs12SignatureToken("password", pkcs12TokenFile);
        privateKey = signingToken.getKeys().get(0);
    }

}
