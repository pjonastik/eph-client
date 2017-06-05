package sk.fri.uniza;

import sk.fri.uniza.utils.UserMessage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class FilePostingSheetFactory {

    public static PostingSheet getInstance(String postingSheetFilePath) {
        try{
            return tryCreateInstance(postingSheetFilePath);
        } catch (IOException e){
            throw new FilePostingSheetException(UserMessage.IO_ERROR_DURING_READING_POSTING_SHEET);
        }
    }

    private static PostingSheet tryCreateInstance(String filePath) throws IOException {
        String mimeType = detectMIMEtypeOfFile(filePath);
        if(mimeType == null)
            throw new FilePostingSheetException(UserMessage.FILE_NOT_FOUND_OR_UNKOWN_MIME_TYPE +" '"+ filePath+"'");

        if(mimeType.equals("application/xml")){
            return createXMLPostingSheet(filePath);
        }
        throw new FilePostingSheetException(UserMessage.NOT_SUPPORTED_MIME_TYPE_OF_FILE + " '" + mimeType + "'");
    }

    private static String detectMIMEtypeOfFile(String filePath) throws IOException {
        return Files.probeContentType(FileSystems.getDefault().getPath(filePath));
    }

    private static PostingSheet createXMLPostingSheet(String filePath) throws FileNotFoundException {
        XMLPostingSheet ps = new XMLPostingSheet();
        ps.readOriginalContentFrom(new FileInputStream(filePath));
        return ps;
    }

    public static PostingSheet getInstance(String postingSheetFilePath, String validationInfoFilePath)  {
        try {
            return tryCreateInstance(postingSheetFilePath, validationInfoFilePath);
        } catch (IOException e) {
            throw new FilePostingSheetException(UserMessage.IO_ERROR_DURING_READING_VALIDATION_INFO);
        }
    }

    private static PostingSheet tryCreateInstance(String postingSheetFilePath, String validationInfoFilePath)
            throws IOException {

        String mimeType = detectMIMEtypeOfFile(validationInfoFilePath);
        if(mimeType == null)
            throw new FilePostingSheetException(UserMessage.FILE_NOT_FOUND_OR_UNKOWN_MIME_TYPE + " '"+ validationInfoFilePath+"'");

        if (mimeType.equals("application/xml")) {
            PostingSheet ps = getInstance(postingSheetFilePath);
            ps.readValidationInfoFrom(new FileInputStream(validationInfoFilePath));
            return ps;
        }
        throw new FilePostingSheetException(UserMessage.NOT_SUPPORTED_MIME_TYPE_OF_FILE + " '"+ mimeType +"'");
    }

    public static class FilePostingSheetException extends RuntimeException{
        public FilePostingSheetException(String message) {
            super(message);
        }
    }


}
