package sk.fri.uniza;

import sk.fri.uniza.PostingSheet;

public class SendRequest {
    private String subject;
    private String userName;
    private char [] userPassword;
    private String filingRoomURL;
    private PostingSheet postingSheet;

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(char [] userPassword) {
        this.userPassword = userPassword;
    }

    public char [] getUserPassword() {
        return userPassword;
    }

    public void setFilingRoomURL(String filingRoomURL) {
        this.filingRoomURL = filingRoomURL;
    }

    public String getFilingRoomURL() {
        return filingRoomURL;
    }

    public PostingSheet getPostingSheet() {
        return postingSheet;
    }

    public void setPostingSheet(PostingSheet postingSheet) {
        this.postingSheet = postingSheet;
    }
}
