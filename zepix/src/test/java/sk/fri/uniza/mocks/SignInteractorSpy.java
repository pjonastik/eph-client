package sk.fri.uniza.mocks;

import sk.fri.uniza.ObserverInteractor;
import sk.fri.uniza.SignRequest;

public class SignInteractorSpy extends SignInteractorDummy{

    private SignRequest signRequest;
    private boolean registerInvoked;

    @Override
    public void sign(SignRequest signRequest) {
        this.signRequest = signRequest;
    }

    public SignRequest getSpiedSignRequest() {
        return signRequest;
    }

    @Override
    public void registerObserver(ObserverInteractor observer) {
        registerInvoked = true;
    }

    public boolean wasRegisterInvoked() {
        return registerInvoked;
    }
}
