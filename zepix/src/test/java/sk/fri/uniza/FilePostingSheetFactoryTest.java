package sk.fri.uniza;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sk.fri.uniza.utils.UserMessage;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static sk.fri.uniza.testUtils.TestResourceUtil.*;

public class FilePostingSheetFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getInstanceShouldFailed_cantDetectMIMEtypeOfXMLFile() throws Exception {
        String wrongFilePath = "/undetectable/MIMEtype or wrong file path";
        expectedException.expect(FilePostingSheetFactory.FilePostingSheetException.class);
        expectedException.expectMessage(UserMessage.FILE_NOT_FOUND_OR_UNKOWN_MIME_TYPE + " '" + wrongFilePath+"'");
        FilePostingSheetFactory.getInstance(wrongFilePath);
    }

    @Test
    public void getInstanceShouldFailed_notSupportedMIMEtypeOfPostingSheet() throws Exception {
        expectedException.expect(FilePostingSheetFactory.FilePostingSheetException.class);
        expectedException.expectMessage(UserMessage.NOT_SUPPORTED_MIME_TYPE_OF_FILE + " 'application/pdf'");
        FilePostingSheetFactory.getInstance("postingSheet.pdf");
    }

    @Test
    public void getInstanceShouldHaveSameContentAsFile() throws Exception {
        PostingSheet ps = FilePostingSheetFactory.getInstance(relativeResource2AbsolutePath("xml_example.xml"));
        assertThat(ps, is(instanceOf(XMLPostingSheet.class)));
        assertThat(getReadContentOfPostingSheet(ps), is(helloWorldExampleContent()));
        //it doesn't matter if 'new line' is read or not, in XML transformations new lines are removed anyway
    }

    private String getReadContentOfPostingSheet(PostingSheet ps) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ps.writeOriginalContentTo(baos);
        return new String(baos.toByteArray());
    }

    @Test
    public void getInstanceShouldFailed_cantDetectMIMEtypeOfValidationInfo() throws Exception {
        String wrongFilePath = "/undetectable/MIMEtype or wrong file path";
        expectedException.expect(FilePostingSheetFactory.FilePostingSheetException.class);
        expectedException.expectMessage(UserMessage.FILE_NOT_FOUND_OR_UNKOWN_MIME_TYPE + " '" + wrongFilePath+"'");
        String validFilePath = relativeResource2AbsolutePath("xml_example.xml");
        FilePostingSheetFactory.getInstance(validFilePath, wrongFilePath);
    }

    @Test
    public void getInstanceShouldFailed_notSupportedMIMEtypeOfValidationInfo() throws Exception {
        String errMsg = UserMessage.NOT_SUPPORTED_MIME_TYPE_OF_FILE + " '" + "text/plain" +"'";
        expectedException.expect(FilePostingSheetFactory.FilePostingSheetException.class);
        expectedException.expectMessage(errMsg);
        String validFilePath = relativeResource2AbsolutePath("xml_example.xml");
        FilePostingSheetFactory.getInstance(validFilePath, "validationInfo.txt");
    }

    @Test
    public void getInstanceWithValidInfoShouldFailed_cantDetectMIMEtypeOfPostingSheet() throws Exception {
        String wrongFilePath = "/undetectable/MIMEtype or wrong file path";
        expectedException.expect(FilePostingSheetFactory.FilePostingSheetException.class);
        expectedException.expectMessage(UserMessage.FILE_NOT_FOUND_OR_UNKOWN_MIME_TYPE + " '" + wrongFilePath+"'");

        String validValidationInfoPath = relativeResource2AbsolutePath("eph-invalid.xsd");
        FilePostingSheetFactory.getInstance(wrongFilePath, validValidationInfoPath);
    }

    @Test
    public void getInstanceShouldHaveSameContentAsInFiles() throws Exception {
        String validPostingSheetPath = relativeResource2AbsolutePath("eph-invalid.xml");
        String validValidationInfoPath = relativeResource2AbsolutePath("eph-invalid.xsd");
        PostingSheet ps = FilePostingSheetFactory.getInstance(validPostingSheetPath, validValidationInfoPath);
        assertThat(ps, is(instanceOf(XMLPostingSheet.class)));
        assertThat(getReadContentOfPostingSheet(ps), is(invalidXML()));
        assertThat(getReadValidationInfoOfPostingSheet(ps), is(invalidXSD()));

        //notice that we read files with invalid content
        //validation of path is check here, but XML validation exist in different place
    }

    private String getReadValidationInfoOfPostingSheet(PostingSheet ps) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ps.writeValidationInfoTo(baos);
        return new String(baos.toByteArray());
    }

}