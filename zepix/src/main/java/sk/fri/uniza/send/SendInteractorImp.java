package sk.fri.uniza.send;

import sk.fri.uniza.*;
import sk.fri.uniza.utils.UserMessage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static sk.fri.uniza.utils.InternalMessage.POSTING_SHEET_WAS_NOT_SIGNED_YET;

public class SendInteractorImp implements ObserverInteractor, SendInteractor {

    private PostingSheet postingSheet;
    private Sender sender;

    public SendInteractorImp(ObservableInteractor observableInteractor) {
        observableInteractor.registerObserver(this);
    }

    @Override
    public void notifySigningDone(PostingSheet postingSheet) {
        this.postingSheet = postingSheet;
    }

    @Override
    public void send(SendRequest sendRequest) {
        if (postingSheetIsNotSignedYet())
            throw new SendException(POSTING_SHEET_WAS_NOT_SIGNED_YET);

        validateSendRequest(sendRequest);
        sendRequest.setPostingSheet(postingSheet);
        sender.send(sendRequest);
        clearPassword(sendRequest);
    }

    private void clearPassword(SendRequest sendRequest) {
        char [] password = sendRequest.getUserPassword();
        Arrays.fill(password, '\0');
    }

    private boolean postingSheetIsNotSignedYet() {
        return postingSheet == null;
    }

    private void validateSendRequest(SendRequest sendRequest) {
        validateSubject(sendRequest);
        validateUserName(sendRequest);
        validateUserPassword(sendRequest);
        validateFilingRoomURL(sendRequest);
    }

    private void validateSubject(SendRequest sendRequest) {
        if (sendRequest.getSubject().isEmpty())
            throw new SendException(UserMessage.SUBJECT_WAS_NOT_SET);
    }

    private void validateUserName(SendRequest sendRequest) {
        if (sendRequest.getUserName().isEmpty())
            throw new SendException(UserMessage.USER_NAME_WAS_NOT_SET);
    }

    private void validateUserPassword(SendRequest sendRequest) {
        if (sendRequest.getUserPassword().length == 0)
            throw new SendException(UserMessage.USER_PASSWORD_WAS_NOT_SET);
    }

    private void validateFilingRoomURL(SendRequest sendRequest) {
        if(sendRequest.getFilingRoomURL().isEmpty())
            throw new SendException(UserMessage.FILING_ROOM_URL_WAS_NOT_SET);

        try {
            new URL(sendRequest.getFilingRoomURL());
        } catch (MalformedURLException e) {
            throw new SendException(UserMessage.FILING_ROOM_URL_IS_MALFORMED);
        }
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }
}
