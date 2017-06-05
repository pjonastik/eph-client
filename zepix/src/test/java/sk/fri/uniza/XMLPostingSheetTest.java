package sk.fri.uniza;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import sk.fri.uniza.PostingSheet.PostingSheetContentWasNotReadYetException;
import sk.fri.uniza.PostingSheet.PostingSheetSignedContentWasNotReadException;
import sk.fri.uniza.mocks.OutputStreamSpy;
import sk.fri.uniza.mocks.StringInputStreamSpy;
import sk.fri.uniza.testUtils.TestResourceUtil;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;
import static sk.fri.uniza.testUtils.TestResourceUtil.*;

public class XMLPostingSheetTest {

    private final String SIGNED_CONTENT = "this is signed posting sheet";
    private final String ORIGINAL_CONTENT = "this is unsigned posting sheet (i. e. XML)";
    private final String VALIDATION_INFO = "this is validation info (i. e. XSD)";

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private XMLPostingSheet validPostingSheet;

    @Before
    public void setUp() throws Exception {
        validPostingSheet = new XMLPostingSheet();
        validPostingSheet.readOriginalContentFrom(validXMLInputStream());
        validPostingSheet.readValidationInfoFrom(validXSDInputStream());
    }

    /* read */

    @Test
    public void readFromSignedContentShouldReadAndCloseStream() throws Exception {
        StringInputStreamSpy inSpy = new StringInputStreamSpy(createInputStream(SIGNED_CONTENT));
        validPostingSheet.readSignedContentFrom(inSpy);
        assertThat(inSpy.readBytesToString(), is(SIGNED_CONTENT));
        assertThat(inSpy.isClosed(), is(true));
    }

    private ByteArrayInputStream createInputStream(String doc) {
        return new ByteArrayInputStream(doc.getBytes());
    }

    @Test
    public void readOriginalContent_ShouldReadAndCloseStream() throws Exception {
        StringInputStreamSpy inSpy = new StringInputStreamSpy(createInputStream(ORIGINAL_CONTENT));
        validPostingSheet.readOriginalContentFrom(inSpy);
        assertThat(inSpy.readBytesToString(), is(ORIGINAL_CONTENT));
        assertThat(inSpy.isClosed(), is(true));
    }

    @Test
    public void readValidationInfo_ShouldReadAndCloseStream() throws Exception {
        StringInputStreamSpy inSpy = new StringInputStreamSpy(createInputStream(VALIDATION_INFO));
        validPostingSheet.readValidationInfoFrom(inSpy);
        assertThat(inSpy.readBytesToString(), is(VALIDATION_INFO));
        assertThat(inSpy.isClosed(), is(true));
    }

    /* write */
    @Test
    public void writeSignedShouldFailAndCloseStream_WhenSignedConetentWasNotReadBefore() throws Exception {
        OutputStreamSpy outSpy = new OutputStreamSpy();
        try{
            validPostingSheet.writeSignedContentTo(outSpy);
            fail();
        } catch (PostingSheetSignedContentWasNotReadException e){
            assertThat(outSpy.isClosed(), is(true));
        }
    }

    @Test
    public void writingSignedContent_ShouldBeSameAsRetrievedAndCloseStream() throws Exception {
        InputStream input = createInputStream(SIGNED_CONTENT);
        assertThat(validPostingSheet.isSigned(), is(false));
        validPostingSheet.readSignedContentFrom(input);
        assertThat(validPostingSheet.isSigned(), is(true));
        OutputStreamSpy outSpy = new OutputStreamSpy();
        validPostingSheet.writeSignedContentTo(outSpy);
        assertThat(outSpy.wroteBytesToString(), is(SIGNED_CONTENT));
        assertThat(outSpy.isClosed(), is(true));
    }

    @Test
    public void writeOriginalShouldFailAndCloseStream_WhenOriginalContentWasNotReadBefore() throws Exception {
        XMLPostingSheet postingSheet = new XMLPostingSheet();
        OutputStreamSpy outSpy = new OutputStreamSpy();
        try{
            postingSheet.writeOriginalContentTo(outSpy);
            fail();
        } catch (PostingSheetContentWasNotReadYetException e){
            assertThat(outSpy.isClosed(), is(true));
        }
    }

    @Test
    public void writeOriginalContent_ShouldBeSameAsRetrievedAndCloseStream() throws Exception {
        InputStream input = createInputStream(ORIGINAL_CONTENT);
        validPostingSheet.readOriginalContentFrom(input);
        OutputStreamSpy outSpy = new OutputStreamSpy();
        validPostingSheet.writeOriginalContentTo(outSpy);
        assertThat(outSpy.wroteBytesToString(), is(ORIGINAL_CONTENT));
    }

    @Test
    public void writeValidationInfolShouldFailAndCloseStream_WhenContentWasNotReadBefore() throws Exception {
        XMLPostingSheet postingSheet = new XMLPostingSheet();
        OutputStreamSpy outSpy = new OutputStreamSpy();
        try{
            postingSheet.writeValidationInfoTo(outSpy);
            fail();
        } catch (PostingSheetContentWasNotReadYetException e){
            assertThat(outSpy.isClosed(), is(true));
        }
    }

    @Test
    public void writeValidationInfo_ShouldBeSameAsRetrievedAndCloseStream() throws Exception {
        InputStream input = createInputStream(VALIDATION_INFO);
        validPostingSheet.readValidationInfoFrom(input);
        OutputStreamSpy outSpy = new OutputStreamSpy();
        validPostingSheet.writeValidationInfoTo(outSpy);
        assertThat(outSpy.wroteBytesToString(), is(VALIDATION_INFO));
    }

    /* validation */

    @Test
    public void validateShouldFailed_XMLisNotWellFormated() throws Exception {
        expectedException.expect(PostingSheet.InvalidPostingSheetException.class);
        expectedException.expectMessage("validation failed: XML document is not well-formed or not valid!");
        validPostingSheet.readOriginalContentFrom(TestResourceUtil.notWellFormattedXMLInputStream());

        this.validPostingSheet.validate();
    }

    @Test
    public void validateShouldFailed_XSDisNotWellFormated() throws Exception {
        expectedException.expect(PostingSheet.InvalidPostingSheetException.class);
        expectedException.expectMessage("validation failed: XSD document is not well-formed!");
        XMLPostingSheet ps = new XMLPostingSheet();
        ps.readValidationInfoFrom(TestResourceUtil.invalidXSDInputStream());
        ps.readOriginalContentFrom(dummyInputStream());

        ps.validate();
    }

    @Test
    public void validateShouldFailed_XMLisInvalid() throws Exception {
        expectedException.expect(PostingSheet.InvalidPostingSheetException.class);
        expectedException.expectMessage("validation failed: XML document is not well-formed or not valid!");

        validPostingSheet.readOriginalContentFrom(invalidXMLInputStream());

        this.validPostingSheet.validate();

        //Note: Used schema don't allow elements of XML Signature.
        //      It means that already signed document is not valid document!
    }

    @Test
    public void validationShouldPass() throws Exception {
        validPostingSheet.validate();
    }

    @Test
    public void validateShouldBeIdempotentOperationWithSameInput() throws Exception {
        for (int i = 0; i < 5; i++)
            validPostingSheet.validate();
    }

    @Test
    public void validateShouldNotBeIdempotentWithDifferentInput() throws Exception {
        validPostingSheet.validate();

        try {
            validPostingSheet.readOriginalContentFrom(invalidXMLInputStream());
            validPostingSheet.validate();
            fail();
        } catch (Exception e){
            //exception thrown!
        }
    }

    @Test
    public void saveSignedContent() throws Exception {
        File filePath = tmpFolder.newFile();
        validPostingSheet.readSignedContentFrom(createInputStream(SIGNED_CONTENT));
        validPostingSheet.saveSignedContent(filePath);
        String savedContent = readFrom(filePath.getPath());
        assertThat(savedContent, is(SIGNED_CONTENT));
    }

    private String readFrom(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
