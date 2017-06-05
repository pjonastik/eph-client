package sk.fri.uniza.mocks;

import sk.fri.uniza.PostingSheet;
import sk.fri.uniza.SignRequest;

public class InvalidPostingSheetSignInteractorStub extends SignInteractorDummy {
    @Override
    public void sign(SignRequest signRequest) {
        throw new PostingSheet.InvalidPostingSheetException();
    }
}
