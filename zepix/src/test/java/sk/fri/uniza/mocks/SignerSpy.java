package sk.fri.uniza.mocks;

import sk.fri.uniza.*;

public class SignerSpy extends SignerStub{
    protected SignRequest passedSignRequest;

    @Override
    public PostingSheet signPostingSheet(SignRequest parameters) {
        this.passedSignRequest = parameters;
        return null;
    }

    public SignRequest getSpiedSignRequest() {
        return passedSignRequest;
    }

}
