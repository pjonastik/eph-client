package sk.fri.uniza.save;

import sk.fri.uniza.*;
import sk.fri.uniza.utils.UserMessage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static sk.fri.uniza.utils.InternalMessage.POSTING_SHEET_WAS_NOT_SIGNED_YET;

public class SaveInteractorImp implements ObserverInteractor, SaveInteractor {

    private PostingSheet signedPostingSheet;

    public SaveInteractorImp(ObservableInteractor observable) {
        observable.registerObserver(this);
    }

    @Override
    public void notifySigningDone(PostingSheet postingSheet) {
        signedPostingSheet = postingSheet;
    }

    @Override
    public void save(File filePath) {
        if(postingSheetIsNotSignedYet()) {
            throw new SaveException(POSTING_SHEET_WAS_NOT_SIGNED_YET);
        }

        validateFilePath(filePath);
        signedPostingSheet.saveSignedContent(filePath);
    }

    private boolean postingSheetIsNotSignedYet() {
        return signedPostingSheet == null;
    }

    private void validateFilePath(File filePath) {
        if (filePath.getPath().isEmpty())
            throw new SaveException(UserMessage.SIGNED_POSTING_SHEET_PATH_WAS_NOT_SET);

        Path path = filePath.toPath().toAbsolutePath();
        validateParent(path);
        if (Files.exists(path))
            throw new SaveException(UserMessage.FILE_EXISTS + " '" + path + "'");
    }

    private void validateParent(Path path) {
        Path parent = path.getParent();
        if (pathNotExistsOrIsNotDirectory(parent))
            throw new SaveException(UserMessage.DIR_NOT_FOUND + " '" + parent + "'");
    }

    private boolean pathNotExistsOrIsNotDirectory(Path parent) {
        return !Files.exists(parent) || !Files.isDirectory(parent);
    }
}
