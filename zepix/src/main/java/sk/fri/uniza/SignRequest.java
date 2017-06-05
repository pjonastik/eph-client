package sk.fri.uniza;

import sk.fri.uniza.Signer.DigestAlgorithm;
import sk.fri.uniza.Signer.SignatureLevel;
import sk.fri.uniza.Signer.SignatureStructure;

import java.util.Date;

public class SignRequest {

    private PostingSheet postingSheet;
    private SignaturePolicy signaturePolicy;
    private SignatureStructure signatureStructure;
    private DigestAlgorithm digestAlgorithm;
    private SignatureLevel signatureLevel;
    private String filePathToToken;
    private char[] passwordToP12;
    private Date signingDate;

    public void validate() {
        postingSheet.validate();
    }

    public PostingSheet getPostingSheet() {
        return postingSheet;
    }

    public String getFilePathToP12() {
        return filePathToToken;
    }

    public char[] getPasswordToP12() {
        return passwordToP12;
    }

    public SignaturePolicy getSignaturePolicy() {
        return signaturePolicy;
    }

    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    public SignatureStructure getSignatureStructure() {
        return signatureStructure;
    }

    public SignatureLevel getSignatureLevel() {
        return signatureLevel;
    }

    public Date getSigningDate() {
        return signingDate;
    }

    public void setPostingSheet(PostingSheet postingSheet) {
        this.postingSheet = postingSheet;
    }

    public void setFilePathToP12(String filePathToToken) {
        this.filePathToToken = filePathToToken;
    }

    public void setPasswordToP12(char[] passwordToP12) {
        this.passwordToP12 = passwordToP12;
    }

    public void setSignaturePolicy(SignaturePolicy signaturePolicy) {
        this.signaturePolicy = signaturePolicy;
    }

    public void setDigestAlgorithm(Signer.DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public void setSignatureStructure(SignatureStructure signatureStructure) {
        this.signatureStructure = signatureStructure;
    }

    public void setSignatureLevel(SignatureLevel signatureLevel) {
        this.signatureLevel = signatureLevel;
    }

    public void setSigningDate(Date signingDate) {
        this.signingDate = signingDate;
    }


}
