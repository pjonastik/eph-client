package sk.fri.uniza.sign;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sk.fri.uniza.*;
import sk.fri.uniza.gui.SignPresenterSpy;
import sk.fri.uniza.gui.SignPresenterStub;
import sk.fri.uniza.mocks.*;
import sk.fri.uniza.sign.SignInteractorImp;
import sk.fri.uniza.utils.UserMessage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class SignInteractorImpTest extends PostingSheetDummy implements Signer {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SignInteractorImp signInteractor;

    private int orderIndex;
    private int validateOrderNr;
    private int signPostingSheetOrderNr;

    /* PostingSheetSpy methods */

    @Override
    public void validate() throws InvalidPostingSheetException {
        validateOrderNr = orderIndex++;
    }

    @Before
    public void setUp() throws Exception {
        signInteractor = new SignInteractorImp();
        signInteractor.setSigner(new SignerStub());
        signInteractor.setPresenter(new SignPresenterStub());
        orderIndex = signPostingSheetOrderNr = validateOrderNr = 0;
    }

    /* Signer methods */
    @Override
    public PostingSheet signPostingSheet(SignRequest parameters) {
        signPostingSheetOrderNr = orderIndex++;
        return null;
    }

    @Override
    public Collection<SignaturePolicy> getSignaturePolicies() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<DigestAlgorithm> getDigestAlgorithms(SignaturePolicy policy) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<SignatureStructure> getSignatureStructures(SignaturePolicy policy) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<SignatureLevel> getSignatureLevels(SignaturePolicy policy) {
        throw new UnsupportedOperationException();
    }

    @Test
    public void shouldImplementSignInteractorInterface() throws Exception {
        SignInteractor i = signInteractor;
    }

    @Test
    public void requestValidationShouldOccurBeforeSigning() throws Exception {
        signInteractor.setSigner(this);
        SignRequestStub reqestStub = new SignRequestStub();
        reqestStub.setPostingSheet(this);
        signInteractor.sign(reqestStub);
        assertThatSignRequestValidationIsCalledAsFirst();
        assertThatSignPostingSheetIsCalledAsSecond();
    }

    private void assertThatSignRequestValidationIsCalledAsFirst() {
        assertThat(validateOrderNr, is(0));
    }

    private void assertThatSignPostingSheetIsCalledAsSecond() {
        assertThat(signPostingSheetOrderNr, is(1));
    }

    @Test
    public void interactorShouldDelegateSignRequestToSigner() throws Exception {
        SignerSpy signerSpy = new SignerSpy();
        signInteractor.setSigner(signerSpy);
        SignRequest signRequest = new SignRequestStub();
        signInteractor.sign(signRequest);
        assertThat(signerSpy.getSpiedSignRequest(), equalTo(signRequest));
    }

    @Test
    public void signShouldCreateResponseWithOutputFromSigner() throws Exception {
        SignerStub signer = new SignerStub();
        PostingSheet dummy = new PostingSheetDummy();
        signer.setReturnValueFromSignPostingSheet(dummy);
        signInteractor.setSigner(signer);
        SignPresenterSpy presenterSpy = new SignPresenterSpy();
        signInteractor.setPresenter(presenterSpy);
        signInteractor.sign(new SignRequestStub());
        assertThat(presenterSpy.wasShowSignResultInvoked(), is(true));
        assertThat(presenterSpy.getSpiedSignResponse().getSignedPostingSheet(), is (dummy));
    }

    @Test
    public void shouldClearPasswordAfterSign() throws Exception {
        char[] password = "password".toCharArray();
        SignRequestStub request = createAndSetSignRequestStub(password);
        signInteractor.sign(request);
        assertThat(request.getPasswordToP12(), is((password)));
        char [] clearedPassword = new char[password.length];
        assertThat(Arrays.toString(request.getPasswordToP12()), is(equalTo(Arrays.toString(clearedPassword))));
    }

    private SignRequestStub createAndSetSignRequestStub(char[] password) {
        SignRequestStub request = new SignRequestStub();
        request.setPassword(password);
        return  request;
    }

    @Test
    public void signShouldFailed_WhenInvalidPostingSheet() throws Exception {
        expectedException.expect(InvalidPostingSheetException.class);
        expectedException.expectMessage(UserMessage.INVALID_POSTING_SHEET);
        SignRequestStub request = new SignRequestStub();
        request.setPostingSheet(new InvalidPostingSheetStub());
        signInteractor.sign(request);
    }

    @Test
    public void signShouldFailed_WhenEmptyFilePathOfP12() throws Exception {
        expectValidationExceptionWithP12FileNotFoundMessage();
        SignRequestStub request = new SignRequestStub();
        request.setFilePathToP12("");
        signInteractor.sign(request);
    }

    private void expectValidationExceptionWithP12FileNotFoundMessage() {
        expectedException.expect(SignInteractorImp.KeyStoreFileNotFoundException.class);
        expectedException.expectMessage(UserMessage.KEYSTORE_FILE_NOT_FOUND);
    }

    @Test
    public void signShouldFailed_WhenWrongFilePathOfP12() throws Exception {
        expectValidationExceptionWithP12FileNotFoundMessage();
        SignRequestStub request = new SignRequestStub();
        request.setFilePathToP12("wrong file path");
        signInteractor.sign(request);
    }

    @Test
    public void interactorShouldNotCatchKeyStoreAuthentificationException() throws Exception {
        expectedException.expect(Signer.KeyStoreAuthenticationException.class);
        signInteractor.setSigner(new KeyStoreFailSignerStub());
        signInteractor.sign(new SignRequestStub());
    }

    /* Observer tests */

    @Test
    public void shouldImplementSubjectInterface() throws Exception {
        ObservableInteractor s = new SignInteractorImp();
    }

    @Test
    public void shouldNotifyAllObservers_WhenAfterSigning() throws Exception {
        PostingSheet ps = new PostingSheetDummy();
        setSignerToReturnPostingSheet(ps);
        SaveInteractorSpy saveSpy1 = new SaveInteractorSpy();
        SaveInteractorSpy saveSpy2 = new SaveInteractorSpy();
        signInteractor.registerObserver(saveSpy1);
        signInteractor.registerObserver(saveSpy2);
        assertThat(saveSpy1.wasNotifyDoneInvoked(), is(false));
        assertThat(saveSpy2.wasNotifyDoneInvoked(), is(false));
        signInteractor.sign(new SignRequestStub());
        assertThat(saveSpy1.wasNotifyDoneInvoked(), is(true));
        assertThat(saveSpy1.getSpiedPostingSheet(), is(ps));
        assertThat(saveSpy2.wasNotifyDoneInvoked(), is(true));
        assertThat(saveSpy2.getSpiedPostingSheet(), is(ps));
    }

    private void setSignerToReturnPostingSheet(PostingSheet ps) {
        SignerStub signer = new SignerStub();
        signer.setReturnValueFromSignPostingSheet(ps);
        signInteractor.setSigner(signer);
    }
}
