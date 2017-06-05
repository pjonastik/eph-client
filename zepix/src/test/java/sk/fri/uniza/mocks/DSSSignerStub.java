package sk.fri.uniza.mocks;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import sk.fri.uniza.sign.DSSXAdESSigner;

public class DSSSignerStub extends DSSXAdESSigner {

    @Override
    public DSSDocument sign(XAdESSignatureParameters parameters, DSSDocument toSignDocument, SignatureTokenConnection signingToken) {
        return new InMemoryDocument("signed content".getBytes());
    }
}
