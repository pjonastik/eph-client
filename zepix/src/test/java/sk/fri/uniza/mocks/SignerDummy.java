package sk.fri.uniza.mocks;

import sk.fri.uniza.*;

import java.util.Collection;

public class SignerDummy implements Signer {
    @Override
    public PostingSheet signPostingSheet(SignRequest parameters) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public Collection<SignaturePolicy> getSignaturePolicies() {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public Collection<DigestAlgorithm> getDigestAlgorithms(SignaturePolicy policy) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public Collection<SignatureStructure> getSignatureStructures(SignaturePolicy policy) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public Collection<SignatureLevel> getSignatureLevels(SignaturePolicy policy) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }
}
