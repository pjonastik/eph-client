package sk.fri.uniza.mocks;

import sk.fri.uniza.ObserverInteractor;
import sk.fri.uniza.PostingSheet;

import java.io.File;

public class SaveInteractorDummy implements sk.fri.uniza.SaveInteractor, ObserverInteractor {

    @Override
    public void save(File filePath) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public void notifySigningDone(PostingSheet postingSheet) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }
}
