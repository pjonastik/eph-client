package sk.fri.uniza.gui;

import sk.fri.uniza.SignResponse;

public class SignPresenterSpy extends SignPresenterStub {

    private boolean showSignedResultInvoked;
    private SignResponse signResponse;
;
    @Override
    public void tellPostingSheetSigned(SignResponse response) {
        showSignedResultInvoked = true;
        signResponse = response;
    }

    public boolean wasShowSignResultInvoked() {
        return showSignedResultInvoked;
    }

    public SignResponse getSpiedSignResponse() {
        return signResponse;
    }


}
