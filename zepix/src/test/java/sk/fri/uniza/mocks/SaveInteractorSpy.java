package sk.fri.uniza.mocks;

import sk.fri.uniza.PostingSheet;

import java.io.File;

public class SaveInteractorSpy extends SaveInteractorDummy{

    private boolean saveInvoked;
    private File spiedPath;
    private boolean notifyDoneInvoked;
    private PostingSheet spiedPostingSheet;

    @Override
    public void save(File filePath) {
        saveInvoked = true;
        spiedPath = filePath;
    }

    public boolean wasSaveInvoked() {
        return saveInvoked;
    }

    public File getSpiedPath() {
        return spiedPath;
    }

    @Override
    public void notifySigningDone(PostingSheet postingSheet) {
        notifyDoneInvoked = true;
        spiedPostingSheet = postingSheet;
    }

    public boolean wasNotifyDoneInvoked() {
        return notifyDoneInvoked;
    }

    public PostingSheet getSpiedPostingSheet() {
        return spiedPostingSheet;
    }
}
