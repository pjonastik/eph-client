package sk.fri.uniza.mocks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.fail;
import static sk.fri.uniza.testUtils.TestResourceUtil.helloWorldExampleContent;

public class PostingSheetStub extends PostingSheetDummy {

    private boolean signed;

    @Override
    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    @Override
    public void validate() throws InvalidPostingSheetException {
        //valid - not throwing any exceptions
    }

    @Override
    public void writeOriginalContentTo(OutputStream outputStream) {
        try {
            outputStream.write(helloWorldExampleContent().getBytes());
        } catch (IOException e) {
            fail();
        }finally {
            tryCloseStream(outputStream);
        }
    }

    private void tryCloseStream(OutputStream outputStream) {
        try {
            outputStream.close();
        } catch (IOException e) {
            fail();
        }
    }

    @Override
    public void readSignedContentFrom(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            fail();
        }
    }

    @Override
    public void writeSignedContentTo(OutputStream outputStream) {

    }
}
