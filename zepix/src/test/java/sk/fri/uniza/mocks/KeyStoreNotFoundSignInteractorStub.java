package sk.fri.uniza.mocks;

import sk.fri.uniza.SignRequest;

public class KeyStoreNotFoundSignInteractorStub extends SignInteractorDummy {
    @Override
    public void sign(SignRequest signRequest) {
        throw new KeyStoreFileNotFoundException();
    }
}
