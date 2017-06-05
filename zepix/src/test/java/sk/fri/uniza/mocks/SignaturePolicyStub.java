package sk.fri.uniza.mocks;

import sk.fri.uniza.Signer.DigestAlgorithm;
import sk.fri.uniza.testUtils.TestResourceUtil;

public class SignaturePolicyStub extends SignaturePolicyDummy {
    private String OID = TestResourceUtil.POLICY_OID;
    private DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA1;
    private byte[] digestValue = TestResourceUtil.POLICY_DIGEST;

    @Override
    public void validate() {
    }

    @Override
    public String getOID() {
        return OID;
    }

    @Override
    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    @Override
    public byte[] getDigestValue() {
        return digestValue;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public void setDigestAlgorithm(DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public void setDigestValue(byte[] digestValue) {
        this.digestValue = digestValue;
    }
}
