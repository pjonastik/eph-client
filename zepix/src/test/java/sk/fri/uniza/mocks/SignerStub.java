package sk.fri.uniza.mocks;

import sk.fri.uniza.PostingSheet;
import sk.fri.uniza.SignRequest;
import sk.fri.uniza.SignaturePolicy;

import java.util.Arrays;
import java.util.Collection;

public class SignerStub extends SignerDummy {
    private PostingSheet returnValueFromSignPostingSheet;
    protected Collection<SignaturePolicy> signaturePolicies = Arrays.asList(new SignaturePolicyDummy(), new SignaturePolicyDummy());
    protected Collection<DigestAlgorithm> digestAlgs;
    protected Collection<SignatureStructure> structures;
    protected Collection<SignatureLevel> levels;

    public SignerStub() {
    }

    public void setReturnValueFromSignPostingSheet(PostingSheet returnValue) {
        returnValueFromSignPostingSheet = returnValue;
    }

    @Override
    public PostingSheet signPostingSheet(SignRequest parameters) {
        return returnValueFromSignPostingSheet;
    }

    @Override
    public Collection<SignaturePolicy> getSignaturePolicies() {
        return signaturePolicies;
    }

    public SignerStub setSignaturePolicies(Collection<SignaturePolicy> signaturePolicy) {
        this.signaturePolicies = signaturePolicy;
        return this;
    }

    @Override
    public Collection<DigestAlgorithm> getDigestAlgorithms(SignaturePolicy policy) {
        return digestAlgs;
    }


    @Override
    public Collection<SignatureStructure> getSignatureStructures(SignaturePolicy policy) {
        return structures;
    }

    @Override
    public Collection<SignatureLevel> getSignatureLevels(SignaturePolicy policy) {
        return levels;
    }
}
