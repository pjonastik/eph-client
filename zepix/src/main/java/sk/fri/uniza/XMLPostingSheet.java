package sk.fri.uniza;

import org.xml.sax.SAXException;
import sk.fri.uniza.PostingSheet;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

public class XMLPostingSheet implements PostingSheet {

    private String originalContent;
    private String xsdContent;
    private String signedContent;

    private boolean validResult;

    @Override
    public boolean isSigned() {
        return signedContent != null;
    }

    /*read*/

    @Override
    public void readOriginalContentFrom(InputStream input) {
        try {
            originalContent = reading(input);
            validResult = false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(input);
        }
    }

    @Override
    public void readSignedContentFrom(InputStream signedDoc) {
        try {
            signedContent = reading(signedDoc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(signedDoc);
        }
    }

    private String reading(InputStream signedDoc) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(signedDoc));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null){
            sb.append(s);
        }
        return sb.toString();
    }

    private void closeStream(InputStream signedDoc) {
        try {
            signedDoc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readValidationInfoFrom(InputStream inputStream) {
        try {
            xsdContent = reading(inputStream);
            validResult = false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(inputStream);
        }
    }

    /*write */

    @Override
    public void writeSignedContentTo(OutputStream out) {
        try {
            tryWriteSignedContent(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(out);
        }
    }

    private void tryWriteSignedContent(OutputStream out) throws IOException {
        if(!isSigned())
            throw new PostingSheetSignedContentWasNotReadException();

        bufferedWrite(out, signedContent);
    }

    private void bufferedWrite(OutputStream out, String content) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        bw.write(content);
        bw.flush();
    }

    private void closeStream(OutputStream out) {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeOriginalContentTo(OutputStream out) {
        try {
            tryWriteOriginalContent(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(out);
        }
    }

    private void tryWriteOriginalContent(OutputStream out) throws IOException {
        if (isOriginalContentEmpty())
            throw new PostingSheetContentWasNotReadYetException();

        bufferedWrite(out, originalContent);
    }

    private boolean isOriginalContentEmpty() {
        return originalContent == null;
    }

    @Override
    public void writeValidationInfoTo(OutputStream out) {
        try{
            tryWriteValidationInfo(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(out);
        }
    }

    private void tryWriteValidationInfo(OutputStream out) throws IOException {
        if (xsdContent == null)
            throw new PostingSheetContentWasNotReadYetException();

        bufferedWrite(out, xsdContent);
    }

    @Override
    public void validate() throws InvalidPostingSheetException {
        if (isSchemaSetAndNotValidatedYet()) {
            StreamSource ss = new StreamSource(new StringReader(originalContent));
            StreamSource xsdSS =  new StreamSource(new StringReader(xsdContent));
            validate(ss, createSchema(xsdSS));
            validResult = true;
        }
    }

    private boolean isSchemaSetAndNotValidatedYet() {
        return (xsdContent != null) && !validResult;
    }

    private Schema createSchema(Source schemaSource) {
        try {
            return tryLoadSchema(schemaSource);
        } catch (org.xml.sax.SAXException e) {
            throwExceptionIfFileNotFound("validation failed: problem to locate XSD file!", e);
            throw new InvalidPostingSheetException("validation failed: XSD document is not well-formed!", e);
        }
    }

    private void throwExceptionIfFileNotFound(String m, SAXException e) {
        if (e.getCause() != null && e.getCause() instanceof FileNotFoundException)
            throw new InvalidPostingSheetException(m, e);
    }

    private Schema tryLoadSchema(Source schemaSource) throws org.xml.sax.SAXException {
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return schemaFactory.newSchema(schemaSource);
    }

    private void validate(Source xmlFile, Schema schema) throws InvalidPostingSheetException {
        try {
            tryValidate(xmlFile, schema);
        } catch (org.xml.sax.SAXException e) {
            throw new InvalidPostingSheetException("validation failed: XML document is not well-formed or not valid!", e);
        } catch (IOException e) {
            throw new InvalidPostingSheetException("validation failed: problem to retrieve XML file!", e);
        }
    }

    private void tryValidate(Source xmlFile, Schema schema) throws org.xml.sax.SAXException, IOException {
        Validator validator = schema.newValidator();
        validator.validate(xmlFile);
    }

    @Override
    public void saveSignedContent(File filePath) {
        try {
            tryWriteSignedContent(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
