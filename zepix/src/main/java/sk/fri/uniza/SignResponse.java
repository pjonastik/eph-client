package sk.fri.uniza;

public class SignResponse {

    private PostingSheet signedPostingSheet;

    public SignResponse() {
    }

    public SignResponse(PostingSheet signedPostingSheet) {
        this.signedPostingSheet = signedPostingSheet;
    }

    public PostingSheet getSignedPostingSheet() {
        return signedPostingSheet;
    }

    public void setSignedPostingSheet(PostingSheet signedPostingSheet) {
        this.signedPostingSheet = signedPostingSheet;
    }
}
