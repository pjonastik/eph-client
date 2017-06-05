package sk.fri.uniza.mocks;

import sk.fri.uniza.*;
import sk.fri.uniza.testUtils.TestResourceUtil;

import java.util.Date;

import static sk.fri.uniza.Signer.*;

public class SignRequestStub extends SignRequest {
    private PostingSheet postingSheet = new PostingSheetStub();

    private SignaturePolicy policy = new SignaturePolicyStub();

    private String filePathToP12 = TestResourceUtil.VALID_KEYSTORE_FILEPATH;
    private char[] password = new char[0];


    @Override
    public PostingSheet getPostingSheet() {
        return postingSheet;
    }

    public void setPostingSheet(PostingSheet postingSheet) {
        this.postingSheet = postingSheet;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public void setFilePathToP12(String filePathToP12) {
        this.filePathToP12 = filePathToP12;
    }

    @Override
    public SignaturePolicy getSignaturePolicy() {
        return policy;
    }

    @Override
    public DigestAlgorithm getDigestAlgorithm() {
        return DigestAlgorithm.SHA1;
    }

    @Override
    public SignatureStructure getSignatureStructure() {
        return SignatureStructure.ENVELOPED;
    }

    @Override
    public SignatureLevel getSignatureLevel() {
        return SignatureLevel.B;
    }

    @Override
    public String getFilePathToP12() {
        return filePathToP12;
    }

    @Override
    public char[] getPasswordToP12() {
        return password;
    }

    @Override
    public Date getSigningDate() {
        return null;
    }

    public void setSignaturePolicy(SignaturePolicy policy) {
        this.policy = policy;
    }
}
