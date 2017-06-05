package sk.fri.uniza;

import sk.fri.uniza.utils.UserMessage;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface PostingSheet {

    void readOriginalContentFrom(InputStream inputStream);

    void writeOriginalContentTo(OutputStream outputStream);

    void readValidationInfoFrom(InputStream inputStream);

    void writeValidationInfoTo(OutputStream outputStream);

    void readSignedContentFrom(InputStream inputStream);

    void writeSignedContentTo(OutputStream outputStream);

    void validate() throws InvalidPostingSheetException;

    boolean isSigned();

    void saveSignedContent(File path);

    class InvalidPostingSheetException extends RuntimeException{

        public InvalidPostingSheetException() {
            super(UserMessage.INVALID_POSTING_SHEET);
        }

        public InvalidPostingSheetException(String message, Throwable e) {
            super(message, e);
        }
    }

    class PostingSheetSignedContentWasNotReadException extends RuntimeException{
    }

    class PostingSheetContentWasNotReadYetException extends RuntimeException{
    }
}
