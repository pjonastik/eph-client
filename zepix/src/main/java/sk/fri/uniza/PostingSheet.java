package sk.fri.uniza;

import org.xml.sax.SAXException;

import java.io.File;

public interface PostingSheet {

    void validate() throws InvalidPostingSheetException;

    class InvalidPostingSheetException extends RuntimeException{
        public InvalidPostingSheetException(String message, Throwable e) {
            super(message, e);
        }
    }
}
