package sk.fri.uniza;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static sk.fri.uniza.TestUtil.*;

public class XMLPostingSheetTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private XMLPostingSheet postingSheet;

    @Before
    public void setUp() throws Exception {
        postingSheet = new XMLPostingSheet(loadFileWithName("eph-valid.xsd"));
    }

    @Test
    public void cantCallValidateBeforeCompletelyInit() throws Exception {
        expectedException.expect(XMLPostingSheet.InitializationException.class);
        expectedException.expectMessage("Posting sheet XML file wasn't set!");

        postingSheet.validate();
    }

    @Test
    public void validateShouldFailed_WrongFilePathOfXSD() throws Exception {
        expectedException.expect(PostingSheet.InvalidPostingSheetException.class);
        expectedException.expectMessage("validation failed: problem to locate XSD file!");
        XMLPostingSheet v = new XMLPostingSheet(new File("invalid path"));
        v.setXmlPostingSheet(new File("test double"));

        v.validate();
    }

    @Test
    public void validateShouldFailed_XSDisNotWellFormated() throws Exception {
        expectedException.expect(PostingSheet.InvalidPostingSheetException.class);
        expectedException.expectMessage("validation failed: XSD document is not well-formed!");
        XMLPostingSheet ps = new XMLPostingSheet(loadFileWithName("eph-invalid.xsd"));
        ps.setXmlPostingSheet(new File("test double file"));

        ps.validate();
    }

    @Test
    public void validateShouldFailed_WrongFilePathOfXML() throws Exception {
        expectedException.expect(PostingSheet.InvalidPostingSheetException.class);
        expectedException.expectMessage("validation failed: problem to retrieve XML file!");
        postingSheet.setXmlPostingSheet(new File("invalid path"));

        this.postingSheet.validate();
    }

    @Test
    public void validateShouldFailed_XMLisNotWellFormated() throws Exception {
        expectedException.expect(PostingSheet.InvalidPostingSheetException.class);
        expectedException.expectMessage("validation failed: XML document is not well-formed or not valid!");
        postingSheet.setXmlPostingSheet(loadFileWithName("eph-not-well-formatted.xml"));

        this.postingSheet.validate();
    }

    @Test
    public void validateShouldFailed_XMLisInvalid() throws Exception {
        expectedException.expect(PostingSheet.InvalidPostingSheetException.class);
        expectedException.expectMessage("validation failed: XML document is not well-formed or not valid!");
        postingSheet.setXmlPostingSheet(loadFileWithName("eph-invalid.xml"));

        this.postingSheet.validate();
    }

    @Test
    public void validationShouldPass() throws Exception {
        postingSheet.setXmlPostingSheet(loadFileWithName("eph-valid.xml"));

        postingSheet.validate();
    }

    @Test
    public void validateShouldBeIdempotentOperationWithSameInput() throws Exception {
        postingSheet.setXmlPostingSheet(loadFileWithName("eph-valid.xml"));

        for (int i = 0; i < 5; i++)
            postingSheet.validate();
    }

    @Test
    public void validateShouldNotBeIdempotentWithDifferentInput() throws Exception {
        postingSheet.setXmlPostingSheet(loadFileWithName("eph-valid.xml"));
        postingSheet.validate();

        try {
            postingSheet.setXmlPostingSheet(loadFileWithName("eph-invalid.xml"));
            postingSheet.validate();
            Assert.fail();
        } catch (Exception e){
            //exception thrown!
        }
    }
}
