package sk.fri.uniza.save;

import org.junit.*;
import org.junit.rules.ExpectedException;
import sk.fri.uniza.ObserverInteractor;
import sk.fri.uniza.SaveInteractor;
import sk.fri.uniza.testUtils.TestResourceUtil;
import sk.fri.uniza.mocks.*;
import sk.fri.uniza.utils.InternalMessage;
import sk.fri.uniza.utils.UserMessage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SaveInteractorImpTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private SaveInteractorImp saveInteractorImp;

    @Before
    public void setUp() throws Exception {
        saveInteractorImp = new SaveInteractorImp(new SignInteractorStub());
    }

    @Test
    public void shouldImplementObserverInteractorAndSaveInteractorInterface() throws Exception {
        ObserverInteractor o = saveInteractorImp;
        SaveInteractor s = saveInteractorImp;
    }

    @Test
    public void shouldRegisterToObservableInteractorInConstructor() throws Exception {
        SignInteractorSpy signInteractorSpy = new SignInteractorSpy();
        new SaveInteractorImp(signInteractorSpy);
        assertThat(signInteractorSpy.wasRegisterInvoked(), is(true));
    }

    @Test
    public void saveShouldFail_WhenEmptyPath() throws Exception {
        notifySigningDone();
        String path = "";
        String errMsg = UserMessage.SIGNED_POSTING_SHEET_PATH_WAS_NOT_SET;
        expectErrMessageAndSave(path, errMsg);
    }

    private void notifySigningDone() {
        PostingSheetStub postingSheetStub = new PostingSheetStub();
        postingSheetStub.setSigned(true);
        saveInteractorImp.notifySigningDone(postingSheetStub);
    }

    private void expectErrMessageAndSave(String path, String msg) {
        expectSaveExceptionWithMessage(msg);
        saveInteractorImp.save(new File(path));
    }

    private void expectSaveExceptionWithMessage(String msg) {
        expectedException.expect(SaveInteractorImp.SaveException.class);
        expectedException.expectMessage(msg);
    }

    @Test
    public void saveShouldFail_WhenParentFilePathNotExisting() throws Exception {
        notifySigningDone();
        String wrongParentFilePath = "not/existing/path";
        String wrongFilePath = wrongParentFilePath+ "/newFile.xml";
        Path path = Paths.get(wrongParentFilePath).toAbsolutePath();
        expectErrMessageAndSave(wrongFilePath, UserMessage.DIR_NOT_FOUND+ " '" + path + "'");
    }

    @Test
    public void saveShouldFail_WhenParentFilePathIsNotDir() throws Exception {
        notifySigningDone();
        String parent = TestResourceUtil.relativeResource2AbsolutePath("xml_example.xml");
        String path = parent + "/newFile.xml";
        expectErrMessageAndSave(path, UserMessage.DIR_NOT_FOUND + " '" + parent + "'");
    }

    @Test
    public void saveShouldFail_WhenExistingFile() throws Exception {
        notifySigningDone();
        String path = TestResourceUtil.relativeResource2AbsolutePath("xml_example.xml");
        expectErrMessageAndSave(path, UserMessage.FILE_EXISTS + " '" + path + "'");
    }

    @Test
    public void saveShouldFail_WhenIsInvokedBeforeSigning() throws Exception {
        expectSaveExceptionWithMessage(InternalMessage.POSTING_SHEET_WAS_NOT_SIGNED_YET);
        File path = new File("newFile.xml");
        saveInteractorImp.save(path);
    }

    @Test
    public void saveShouldBeInvoked() throws Exception {
        PostingSheetSpy ps = new PostingSheetSpy();
        ps.setSigned(true);
        saveInteractorImp.notifySigningDone(ps);
        saveInteractorImp.save(new File("newFile.xml"));
        assertThat(ps.isSaveSignedContentInvoked(), is(true));
    }

}
