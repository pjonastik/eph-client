package sk.fri.uniza.send;

import org.junit.Test;
import sk.fri.uniza.SendRequest;
import sk.fri.uniza.testUtils.SendRequestHelper;
import sk.fri.uniza.mocks.PostingSheetStub;
import sk.fri.uniza.Sender;

public class RESTSenderTest {
    @Test
    public void canInit() throws Exception {
        new RESTSender();
    }

    @Test
    public void shouldImplementSenderInterface() throws Exception {
        Sender sender = new RESTSender();
    }

    @Test
    public void sendShouldDoNotFail_IsOnlySimulation() throws Exception {
        SendRequest request = SendRequestHelper.getInstance();
        request.setPostingSheet(new PostingSheetStub());
        Sender sender = new RESTSender();
        sender.send(request);
    }
}
