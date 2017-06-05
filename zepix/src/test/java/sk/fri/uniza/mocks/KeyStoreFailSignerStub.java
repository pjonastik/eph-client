package sk.fri.uniza.mocks;

import sk.fri.uniza.PostingSheet;
import sk.fri.uniza.SignRequest;

public class KeyStoreFailSignerStub extends SignerDummy {
    @Override
    public PostingSheet signPostingSheet(SignRequest parameters) {
        throw new KeyStoreAuthenticationException(null);
    }
}
