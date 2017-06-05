package sk.fri.uniza.mocks;


import java.io.File;

public class PostingSheetSpy extends PostingSheetStub {
    private boolean saveSignedContentInvoked;

    @Override
    public void saveSignedContent(File filePath) {
        saveSignedContentInvoked = true;
    }

    public boolean isSaveSignedContentInvoked() {
        return saveSignedContentInvoked;
    }
}
