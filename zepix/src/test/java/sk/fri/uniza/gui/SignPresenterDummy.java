package sk.fri.uniza.gui;

import sk.fri.uniza.SignPresenter;
import sk.fri.uniza.SignResponse;

public class SignPresenterDummy implements SignPresenter {
    @Override
    public void tellPostingSheetSigned(SignResponse response) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }
}
