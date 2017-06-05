package sk.fri.uniza.mocks;

import sk.fri.uniza.SendInteractor;
import sk.fri.uniza.SendRequest;

public class SendInteractorImpSpy implements SendInteractor {
    private boolean sendInvoked;
    private SendRequest spiedSendRequest;

    @Override
    public void send(SendRequest sendRequest) {
        sendInvoked = true;
        spiedSendRequest = sendRequest;
    }

    public boolean wasSendInvoked() {
        return sendInvoked;
    }

    public SendRequest getSpiedSendRequest() {
        return spiedSendRequest;
    }
}
