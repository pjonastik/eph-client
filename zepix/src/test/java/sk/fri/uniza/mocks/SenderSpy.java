package sk.fri.uniza.mocks;

import sk.fri.uniza.SendRequest;
import sk.fri.uniza.Sender;

public class SenderSpy implements Sender {

    private boolean sendInvoked;
    private SendRequest spiedSendReqest;

    @Override
    public void send(SendRequest sendRequest) {
        sendInvoked = true;
        spiedSendReqest = sendRequest;
    }

    public boolean sendWasInvoked() {
        return sendInvoked;
    }

    public SendRequest spiedSendReqest() {
        return spiedSendReqest;
    }
}
