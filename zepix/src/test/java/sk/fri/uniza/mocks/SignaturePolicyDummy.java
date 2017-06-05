package sk.fri.uniza.mocks;

import sk.fri.uniza.SignaturePolicy;
import sk.fri.uniza.Signer;

public class SignaturePolicyDummy implements SignaturePolicy {
    @Override
    public void validate() {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public String getOID() {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public Signer.DigestAlgorithm getDigestAlgorithm() {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public byte[] getDigestValue() {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("dummy can't call methods");
    }
}
