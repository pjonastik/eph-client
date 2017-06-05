package sk.fri.uniza.mocks;

import sk.fri.uniza.PostingSheet;
import sk.fri.uniza.Signer;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class PostingSheetDummy implements PostingSheet {
    @Override
    public void validate() throws InvalidPostingSheetException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSigned() {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public void saveSignedContent(File filePath) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public void writeSignedContentTo(OutputStream outputStream) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public void readSignedContentFrom(InputStream inputStream) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public void readOriginalContentFrom(InputStream inputStream) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public void writeOriginalContentTo(OutputStream outputStream) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public void readValidationInfoFrom(InputStream inputStream) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }

    @Override
    public void writeValidationInfoTo(OutputStream outputStream) {
        throw new UnsupportedOperationException("dummy can't call methods");
    }
}
