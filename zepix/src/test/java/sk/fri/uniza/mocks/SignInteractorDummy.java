package sk.fri.uniza.mocks;

import sk.fri.uniza.*;
import sk.fri.uniza.SignInteractor;

public class SignInteractorDummy implements SignInteractor, ObservableInteractor {

    @Override
    public void sign(SignRequest signRequest) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public void registerObserver(ObserverInteractor observer) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public void notifyAllObservers(PostingSheet postingSheet) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }
}
