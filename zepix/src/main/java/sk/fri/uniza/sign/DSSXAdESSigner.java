package sk.fri.uniza.sign;

import eu.europa.esig.dss.*;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;


public class DSSXAdESSigner {

    private final CommonCertificateVerifier commonCertificateVerifier;
    private final XAdESService service;

    public DSSXAdESSigner(){
        commonCertificateVerifier = new CommonCertificateVerifier();
        service = new XAdESService(commonCertificateVerifier);
    }

    public DSSDocument sign(XAdESSignatureParameters parameters,
                            DSSDocument toSignDocument,
                            SignatureTokenConnection signingToken)
            throws DSSException {

      DSSPrivateKeyEntry privateKey = signingToken.getKeys().get(0);
      parameters.setSigningCertificate(privateKey.getCertificate());

      ToBeSigned dataToSign = service.getDataToSign(toSignDocument, parameters);
      DigestAlgorithm digestAlg = parameters.getDigestAlgorithm();
      SignatureValue signVal = signingToken.sign(dataToSign, digestAlg, privateKey);
      return service.signDocument(toSignDocument, parameters, signVal);
    }

}
