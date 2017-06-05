package sk.fri.uniza;

import java.io.File;

public interface SaveInteractor {

    void save(File filePath);

    class SaveException extends RuntimeException{
        public SaveException(String fileExists) {
            super(fileExists);
        }
    }
}
