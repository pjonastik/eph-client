package sk.fri.uniza.mocks;

import sk.fri.uniza.SignRequest;
import sk.fri.uniza.Signer;

public class WrongAuthenticationSignInteractorStub extends SignInteractorDummy {
    @Override
    public void sign(SignRequest signRequest) {
        throw new Signer.KeyStoreAuthenticationException(null);
    }
}
