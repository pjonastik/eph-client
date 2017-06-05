package sk.fri.uniza.mocks;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StringInputStreamSpy extends FilterInputStream {
    ByteArrayOutputStream readBytes;
    boolean closed;


    public StringInputStreamSpy(InputStream in){
        super(in);
        readBytes = new ByteArrayOutputStream();
    }

    @Override
    public void close() throws IOException {
        closed = true;
        super.close();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int i = super.read(b, off, len);
        if(i != -1)
            readBytes.write(b, off, i);
        return i;
    }

    public boolean isClosed(){
        return closed;
    }


    public String readBytesToString(){
        return readBytes.toString();
    }
}
