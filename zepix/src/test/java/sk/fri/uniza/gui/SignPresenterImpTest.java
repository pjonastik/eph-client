package sk.fri.uniza.gui;


import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static sk.fri.uniza.Signer.*;

import org.junit.Before;
import org.junit.Test;
import sk.fri.uniza.*;
import sk.fri.uniza.mocks.*;
import sk.fri.uniza.SignInteractor;
import sk.fri.uniza.SendRequest;
import sk.fri.uniza.XMLPostingSheet;
import sk.fri.uniza.testUtils.RetriedAssert;
import sk.fri.uniza.testUtils.SendRequestHelper;
import sk.fri.uniza.utils.UserMessage;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;


public class SignPresenterImpTest {
    private static final int RETRIED_ASSERT_TIME_OUT = 1000; //miliseconds
    private static final int RETRIED_ASSERT_INTERVAL = 250; //miliseconds
    private static final int SLEEP_BEFORE_ASSERT_INTERVAL = 500; //miliseconds

    private SignInteractor signInteractor;
    private Signer signer;
    private SaveInteractor saveInteractor;

    @Before
    public void setUp() throws Exception {
        signInteractor = new SignInteractorDummy();
        signer = new SignerStub();
        saveInteractor = new SaveInteractorDummy();
    }

    @Test
    public void shouldImplementSignPresenter() throws Exception {
        SignPresenter s = new SignPresenterImp(new MainViewStub(), signInteractor, signer, saveInteractor);
    }

    @Test
    public void viewShouldShowFirstPolicyAndItsSignatureParametersAtInitTime() throws Exception {
        MainViewSpy signViewSpy = new MainViewSpy();
        SignerMock signerMock = new SignerMock();
        SignaturePolicy firstPolicy = new SignaturePolicyDummy();
        signerMock.setSignaturePolicies(Arrays.asList(firstPolicy, new SignaturePolicyDummy()));
        signerMock.setExpectedPolicy(firstPolicy);

        new SignPresenterImp(signViewSpy, signInteractor, signerMock, saveInteractor);

        signerMock.verifyThatSignParameterGettersWasInvokedWithExpectedPolicy();
        assertThat(signViewSpy.getShownPolicies(), is(signerMock.getSignaturePolicies()));
        assertThat(signViewSpy.getShownDigestAlgorithms(), is(signerMock.getDigestAlgorithms(firstPolicy)));
        assertThat(signViewSpy.getShownStructures(), is(signerMock.getSignatureStructures(firstPolicy)));
        assertThat(signViewSpy.getShownLevels(), is(signerMock.getSignatureLevels(firstPolicy)));
    }

    @Test
    public void whenPolicySelectedInView_AppropriateMethodsShouldBeInvokedProperly() throws Exception {
        MainViewSpy signViewSpy = new MainViewSpy();
        SignaturePolicy selectedPolicy = new SignaturePolicyDummy();
        SignerMock signerMock = createAndSetMock(selectedPolicy);
        new SignPresenterImp(signViewSpy, signInteractor, signerMock, saveInteractor);
        signViewSpy.resetShownSignatureParameters();

        selectDifferentPolicy(signViewSpy, selectedPolicy);

        signerMock.verifyThatSignParameterGettersWasInvokedWithExpectedPolicy();
        assertThat(signViewSpy.getShownDigestAlgorithms(), is(signerMock.getDigestAlgorithms(selectedPolicy)));
        assertThat(signViewSpy.getShownStructures(), is(signerMock.getSignatureStructures(selectedPolicy)));
        assertThat(signViewSpy.getShownLevels(), is(signerMock.getSignatureLevels(selectedPolicy)));
        assertThat(signViewSpy.wasClearSignedPostingSheetViewStateInvoked(), is(true));
    }

    private SignerMock createAndSetMock(SignaturePolicy selectedPolicy) {
        SignerMock signerMock = new SignerMock();
        signerMock.setExpectedPolicy(selectedPolicy);
        signerMock.setSignaturePolicies(Arrays.asList(new SignaturePolicyDummy(), selectedPolicy));
        signerMock.setOrderNumOfCallToVerify(2);
        return signerMock;
    }

    private void selectDifferentPolicy(MainViewSpy signViewSpy, SignaturePolicy selectedPolicy) {
        ItemSelectable dummySource = createViewComponentDummy();
        int dummyId = 0;
        ItemEvent ie = new ItemEvent(dummySource, dummyId, selectedPolicy, ItemEvent.SELECTED);
        ItemEvent ie2 = new ItemEvent(dummySource, dummyId, null, ItemEvent.DESELECTED);
        signViewSpy.getRegisteredPolicyListener().itemStateChanged(ie);
        signViewSpy.getRegisteredPolicyListener().itemStateChanged(ie2);
    }

    private ItemSelectable createViewComponentDummy(){
        return new ItemSelectable() {
            @Override
            public Object[] getSelectedObjects() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void addItemListener(ItemListener l) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void removeItemListener(ItemListener l) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void whenTellPostingSheetInvoked_ThenCallShouldBeDelegatedToView() throws Exception {
        PostingSheetDummy signedPostingSheet = new PostingSheetDummy();
        SignResponse signResponse = new SignResponse();
        signResponse.setSignedPostingSheet(signedPostingSheet);
        MainViewSpy signViewSpy = new MainViewSpy();
        SaveInteractorSpy saveInteractorSpy = new SaveInteractorSpy();
        SignPresenterImp signPresenterImp = new SignPresenterImp(signViewSpy, signInteractor, signer, saveInteractorSpy);
        signPresenterImp.tellPostingSheetSigned(signResponse);
        assertThat(signViewSpy.wasSetSignedStateInvoked(), is(true));
    }


    /* concurrency tests (Waiting for SwingWorker thread) */

    @Test
    public void whenSaveCmdExecutedAndUserCancelChoosingDialog_ThenSaveOperationShouldBeCanceledAsWell() throws Exception {
        MainViewSpy signViewSpy = new MainViewSpy();
        SaveInteractorSpy saveInteractorSpy = new SaveInteractorSpy();
        new SignPresenterImp(signViewSpy, signInteractor, signer, saveInteractorSpy);
        executeSaveCommand(signViewSpy);
        Thread.sleep(SLEEP_BEFORE_ASSERT_INTERVAL);
        assertThat(saveInteractorSpy.wasSaveInvoked(), is(false));
    }

    @Test
    public void whenSaveCommandExecuted_ThenInteractorSaveShouldBeInvoked() throws Exception {
        File path = new File("singedPostingSheet.xml");
        MainViewSpy signViewSpy = new MainViewSpy();
        signViewSpy.setFilePathForSignedPostingSheet(path);
        SaveInteractorSpy saveInteractorSpy = new SaveInteractorSpy();
        new SignPresenterImp(signViewSpy, signInteractor, signer, saveInteractorSpy);
        executeSaveCommand(signViewSpy);
        retriedAssertThatInteractorSaveWasInvokedProperly(saveInteractorSpy, path);
    }

    private void retriedAssertThatInteractorSaveWasInvokedProperly(SaveInteractorSpy saveInteractorSpy, File path) throws Exception {
        new RetriedAssert(RETRIED_ASSERT_TIME_OUT, RETRIED_ASSERT_INTERVAL) {
            @Override
            public void run() throws Exception {
                assertThat(saveInteractorSpy.wasSaveInvoked(), is(true));
                assertThat(saveInteractorSpy.getSpiedPath(), is(path));
            }
        }.start();
    }

    private void executeSaveCommand(MainViewSpy mainViewSpy) {
        mainViewSpy.getSpiedRegistredSaveListener().actionPerformed(null);
    }

    @Test
    public void whenSignCommandExecutedInView_ThenInteractorSignShouldBeCalled() throws Exception {
        MainViewSpy signViewSpy = new MainViewSpy();
        SignInteractorSpy interactorSpy = new SignInteractorSpy();
        new SignPresenterImp(signViewSpy, interactorSpy, signer, saveInteractor);
        executeSignCommand(signViewSpy);
        retriedAssertThatRequestWasCreatedProperlyAndPassedToInteractor(signViewSpy, interactorSpy);
    }

    private void executeSignCommand(MainViewSpy mainViewSpy) {
        mainViewSpy.getSpiedRegisteredSignListener().actionPerformed(null);
    }

    private void retriedAssertThatRequestWasCreatedProperlyAndPassedToInteractor(MainViewStub signViewStub,
                                                                                 SignInteractorSpy interactorSpy) throws Exception {
        new RetriedAssert(RETRIED_ASSERT_TIME_OUT, RETRIED_ASSERT_INTERVAL){
            public void run() throws Exception {
                SignRequest request =  interactorSpy.getSpiedSignRequest();
                assertThat(request, is(not(nullValue())));
                assertThat(request.getPostingSheet(), is(not(nullValue())));
                assertThat(request.getPostingSheet(), is(instanceOf(XMLPostingSheet.class)));
                assertThat(request.getSignaturePolicy(), is(signViewStub.getSelectedPolicy()));
                assertThat(request.getSignatureStructure(), is(SignatureStructure.NONE));
                assertThat(request.getSignatureLevel(), is(SignatureLevel.NONE));
                assertThat(request.getDigestAlgorithm(), is(DigestAlgorithm.NONE));
                assertThat(request.getFilePathToP12(), is(signViewStub.getFilePathToKeyStore()));
                assertThat(request.getPasswordToP12(), is(signViewStub.getPasswordToKeyStore()));
            }
        }.start();
    }

    @Test
    public void whenSignCommandExecutedInViewShouldFailed_WrongFilePathOfXML() throws Exception {
        MainViewSpy mainViewSpy = new MainViewSpy();
        String wrongFilePath = "XML - invalid or bad file path";
        mainViewSpy.setPostingSheetFilePath(wrongFilePath);
        new SignPresenterImp(mainViewSpy, signInteractor, signer, saveInteractor);
        executeSignCommand(mainViewSpy);
        retriedAssertThatShowErrorWasInvokedAndSpiedErrorIs(mainViewSpy,
                UserMessage.FILE_NOT_FOUND_OR_UNKOWN_MIME_TYPE+" '"+wrongFilePath+"'");
    }

    private void retriedAssertThatShowErrorWasInvokedAndSpiedErrorIs(final MainViewSpy signViewSpy,
                                                                     final String errorMsg) throws Exception {
        new RetriedAssert(RETRIED_ASSERT_TIME_OUT, RETRIED_ASSERT_INTERVAL) {
            public void run() throws Exception {
                assertThat(signViewSpy.wasShowErrorInvoked(), is(true));
                assertThat(signViewSpy.getSpiedErrorMessage(), is(errorMsg));
            }
        }.start();
    }

    @Test
    public void whenSignCommandExecutedInViewShouldFailed_WrongFilePathOfXSD() throws Exception {
        MainViewSpy signViewSpy = new MainViewSpy();
        String wrongFilePath = "XSD - invalid or bad file path";
        signViewSpy.setValidationInfoFilePath(wrongFilePath);
        new SignPresenterImp(signViewSpy, signInteractor, signer, saveInteractor);
        executeSignCommand(signViewSpy);
        retriedAssertThatShowErrorWasInvokedAndSpiedErrorIs(signViewSpy,
                UserMessage.FILE_NOT_FOUND_OR_UNKOWN_MIME_TYPE+" '"+wrongFilePath+"'");
    }

    @Test
    public void showUserValidationError_WhenPostingSheetValidationFail() throws Exception {
        MainViewSpy signViewSpy = new MainViewSpy();
        SignInteractor interactor = new InvalidPostingSheetSignInteractorStub();
        new SignPresenterImp(signViewSpy, interactor, signer, saveInteractor);
        executeSignCommand(signViewSpy);
        retriedAssertThatShowErrorWasInvokedAndSpiedErrorIs(signViewSpy, UserMessage.INVALID_POSTING_SHEET);
    }

    @Test
    public void showToUserValidationError_WhenKeyStoreNotFound() throws Exception {
        MainViewSpy signViewSpy = new MainViewSpy();
        SignInteractor interactor = new KeyStoreNotFoundSignInteractorStub();
        new SignPresenterImp(signViewSpy, interactor, signer, saveInteractor);
        executeSignCommand(signViewSpy);
        retriedAssertThatShowErrorWasInvokedAndSpiedErrorIs(signViewSpy, UserMessage.KEYSTORE_FILE_NOT_FOUND);
    }

    @Test
    public void showToUserValidationError_WhenWrongAuthentication() throws Exception {
        MainViewSpy signViewSpy = new MainViewSpy();
        SignInteractor interactor = new WrongAuthenticationSignInteractorStub();
        new SignPresenterImp(signViewSpy, interactor, signer, saveInteractor);
        executeSignCommand(signViewSpy);
        retriedAssertThatShowErrorWasInvokedAndSpiedErrorIs(signViewSpy, UserMessage.AUTHENTICATION_KEYSTORE_FAILED);
    }

    @Test
    public void whenSendCommandExecuted_ThenInteractorSendShouldBeInvokedProperly() throws Exception {
        SendInteractorImpSpy sendInteractorImpSpy = new SendInteractorImpSpy();
        MainViewSpy mainViewSpy = new MainViewSpy();
        createSignPresenterImp(mainViewSpy, sendInteractorImpSpy);
        executeSendCommand(mainViewSpy);
        retriedAssertThatInteractorSendWasInvoked(sendInteractorImpSpy);
    }

    private void createSignPresenterImp(MainViewSpy mainViewSpy, SendInteractorImpSpy sendInteractorImpSpy) {
        SignPresenterImp signPresenterImp = new SignPresenterImp(mainViewSpy, new SignInteractorDummy(),
                new SignerStub(), new SaveInteractorDummy());
        signPresenterImp.setSendInteractor(sendInteractorImpSpy);
    }

    private void executeSendCommand(MainViewSpy mainViewSpy) {
        mainViewSpy.getSpiedRegistredSendListener().actionPerformed(null);
    }

    private void retriedAssertThatInteractorSendWasInvoked(SendInteractorImpSpy sendInteractorImpSpy) throws Exception {
        new RetriedAssert(RETRIED_ASSERT_TIME_OUT, RETRIED_ASSERT_INTERVAL) {
            @Override
            public void run() throws Exception {
                assertThat(sendInteractorImpSpy.wasSendInvoked(), is(true));
                SendRequest spiedSendRequest = sendInteractorImpSpy.getSpiedSendRequest();
                assertThat(spiedSendRequest, is(notNullValue()));
                assertThat(spiedSendRequest.getFilingRoomURL(), is(SendRequestHelper.FILING_ROOM_URL));
                assertThat(spiedSendRequest.getSubject(), is(SendRequestHelper.SUBJECT));
                assertThat(spiedSendRequest.getUserName(), is(SendRequestHelper.USER_NAME));
                assertThat(spiedSendRequest.getUserPassword(), is(SendRequestHelper.PASSWORD));
            }
        }.start();
    }

}

