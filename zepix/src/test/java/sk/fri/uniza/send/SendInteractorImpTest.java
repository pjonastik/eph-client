package sk.fri.uniza.send;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sk.fri.uniza.*;
import sk.fri.uniza.mocks.*;
import sk.fri.uniza.utils.InternalMessage;
import sk.fri.uniza.testUtils.SendRequestHelper;
import sk.fri.uniza.utils.UserMessage;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SendInteractorImpTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SendInteractorImp sendInteractorImp;

    @Test
    public void shouldImplementObserverInteractorInterface() throws Exception {
        ObserverInteractor o = sendInteractorImp;
    }

    @Before
    public void setUp() throws Exception {
        sendInteractorImp = new SendInteractorImp(new SignInteractorStub());
        sendInteractorImp.setSender(new SenderStub());
    }

    @Test
    public void shouldRegisterObservableInteractor() throws Exception {
        SignInteractorSpy signInteractorSpy = new SignInteractorSpy();
        new SendInteractorImp(signInteractorSpy);
        assertThat(signInteractorSpy.wasRegisterInvoked(), is(true));
    }

    @Test
    public void sendShouldFail_WhenIsInvokedBeforeSigning() throws Exception {
        expectSendExceptionWithMessage(InternalMessage.POSTING_SHEET_WAS_NOT_SIGNED_YET);
        sendInteractorImp.send(new SendRequest());
    }

    private void expectSendExceptionWithMessage(String postingSheetWasNotSignedYet) {
        expectedException.expect(SendInteractor.SendException.class);
        expectedException.expectMessage(postingSheetWasNotSignedYet);
    }

    @Test
    public void sendShouldFail_WhenSenderSubjectIsNotSet() throws Exception {
        expectSendExceptionWithMessage(UserMessage.SUBJECT_WAS_NOT_SET);
        SendRequest sendRequest = new SendRequest();
        sendRequest.setSubject("");
        notifySigningDoneAndSend(sendRequest);
    }

    private void notifySigningDoneAndSend(SendRequest sendRequest) {
        sendInteractorImp.notifySigningDone(new PostingSheetDummy());
        sendInteractorImp.send(sendRequest);
    }

    @Test
    public void sendShouldFail_WhenSenderUsernameIsNotSet() throws Exception {
        expectSendExceptionWithMessage(UserMessage.USER_NAME_WAS_NOT_SET);
        SendRequest sendRequest = SendRequestHelper.getInstance();
        sendRequest.setUserName("");
        notifySigningDoneAndSend(sendRequest);
    }

    @Test
    public void sendShouldFail_WhenSenderPasswordIsNotSet() throws Exception {
        expectSendExceptionWithMessage(UserMessage.USER_PASSWORD_WAS_NOT_SET);
        SendRequest sendRequest = SendRequestHelper.getInstance();
        sendRequest.setPassword(new char[0]);
        notifySigningDoneAndSend(sendRequest);
    }

    @Test
    public void sendShouldFail_WhenFilingRoomURLisNotSet() throws Exception {
        expectSendExceptionWithMessage(UserMessage.FILING_ROOM_URL_WAS_NOT_SET);
        SendRequest sendRequest = SendRequestHelper.getInstance();
        sendRequest.setFilingRoomURL("");
        notifySigningDoneAndSend(sendRequest);
    }

    @Test
    public void sendShouldFail_WhenFilingRoomURLisMalformed() throws Exception {
        expectSendExceptionWithMessage(UserMessage.FILING_ROOM_URL_IS_MALFORMED);
        SendRequest sendRequest = SendRequestHelper.getInstance();
        sendRequest.setFilingRoomURL("malfromed URL");
        notifySigningDoneAndSend(sendRequest);
    }

    @Test
    public void sendShouldInvokedSender() throws Exception {
        SenderSpy senderSpy = new SenderSpy();
        sendInteractorImp.setSender(senderSpy);
        PostingSheetDummy postingSheet = new PostingSheetDummy();
        sendInteractorImp.notifySigningDone(postingSheet);
        SendRequest sendRequest = SendRequestHelper.getInstance();
        sendInteractorImp.send(sendRequest);
        assertThat(senderSpy.sendWasInvoked(), is(true));
        assertThat(senderSpy.spiedSendReqest(), is(sendRequest));
        assertThat(senderSpy.spiedSendReqest().getPostingSheet(), is(postingSheet));
    }

    @Test
    public void shouldClearPasswordAfterSend() throws Exception {
        char[] password = "password".toCharArray();
        SendRequest sendRequest = SendRequestHelper.getInstance();
        sendRequest.setPassword(password);
        notifySigningDoneAndSend(sendRequest);
        assertThat(sendRequest.getUserPassword(), is((password)));
        char [] clearedPassword = new char[password.length];
        assertThat(Arrays.toString(sendRequest.getUserPassword()), is(equalTo(Arrays.toString(clearedPassword))));
    }
}
