package sk.fri.uniza.mocks;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamSpy extends OutputStream {

    private boolean closed;
    private ByteArrayOutputStream writenBytes;
    private FilterOutputStreamSpy innerSpy;

    public OutputStreamSpy() {
        writenBytes = new ByteArrayOutputStream();
        innerSpy = new FilterOutputStreamSpy(writenBytes);
    }

    public String wroteBytesToString() {
        return writenBytes.toString();
    }

    public boolean isClosed(){
        return closed;
    }

    @Override
    public void close() throws IOException {
        innerSpy.close();
    }

    @Override
    public void write(int b) throws IOException {
        innerSpy.write(b);
    }

    private class FilterOutputStreamSpy extends FilterOutputStream {

        public FilterOutputStreamSpy(OutputStream out) {
            super(out);
        }

        @Override
        public void close() throws IOException {
            super.close();
            closed = true;
        }
    }
}
