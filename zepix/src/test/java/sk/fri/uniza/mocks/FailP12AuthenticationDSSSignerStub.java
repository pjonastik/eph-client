package sk.fri.uniza.mocks;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import sk.fri.uniza.sign.DSSXAdESSigner;

import java.io.IOException;
import java.security.UnrecoverableKeyException;

public class FailP12AuthenticationDSSSignerStub extends DSSXAdESSigner {
    @Override
    public DSSDocument sign(XAdESSignatureParameters parameters, DSSDocument toSignDocument,
                            SignatureTokenConnection signingToken) throws DSSException {
        throw new DSSException(new IOException(new UnrecoverableKeyException()));
    }
}
