package sk.fri.uniza;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

public class XMLPostingSheet implements PostingSheet {

    private File xsdPostingSheet;
    private File xmlPostingSheet;
    private boolean validResult;

    public XMLPostingSheet(File xsdPostingSheet) {
        this.xsdPostingSheet = xsdPostingSheet;
    }

    public void validate() throws InvalidPostingSheetException {
        if (!validResult) {
            if (xmlPostingSheet == null)
                throw new InitializationException("Posting sheet XML file wasn't set!");

            Source xmlFile = new StreamSource(xmlPostingSheet);
            Schema schema = createSchema(xsdPostingSheet);
            validate(xmlFile, schema);
            validResult = true;
        }
    }

    private Schema createSchema(File schemaFile) {
        try {
            return tryCreateSchema(schemaFile);
        } catch (org.xml.sax.SAXException e) {
            throwExceptionIfFileNotFound(e, "validation failed: problem to locate XSD file!");
            throw new InvalidPostingSheetException("validation failed: XSD document is not well-formed!", e);
        }
    }

    private void throwExceptionIfFileNotFound(SAXException e, String m) {
        if (e.getCause() != null && e.getCause() instanceof FileNotFoundException)
            throw new InvalidPostingSheetException(m, e);
    }

    private Schema tryCreateSchema(File schemaFile) throws org.xml.sax.SAXException {
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return schemaFactory.newSchema(schemaFile);
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

    public void setXmlPostingSheet(File xmlPostingSheet) {
        this.xmlPostingSheet = xmlPostingSheet;
        validResult = false;
    }

    public class InitializationException extends RuntimeException{
        public InitializationException(String message) {
            super(message);
        }
    }
}
