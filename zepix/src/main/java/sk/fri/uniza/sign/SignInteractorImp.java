package sk.fri.uniza.sign;

import sk.fri.uniza.*;
import sk.fri.uniza.utils.UserMessage;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SignInteractorImp implements SignInteractor, ObservableInteractor {

    private Signer signer;
    private SignPresenter presenter;
    private Set<ObserverInteractor> observers = new HashSet<>();

    public void setSigner(Signer signer) {
        this.signer = signer;
    }

    public void setPresenter(SignPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void sign(SignRequest signRequest) {
        signRequestValidation(signRequest);
        PostingSheet signedPostingSheet = signer.signPostingSheet(signRequest);
        clearPassword(signRequest);
        presenter.tellPostingSheetSigned(new SignResponse(signedPostingSheet));
        notifyAllObservers(signedPostingSheet);
    }

    private void signRequestValidation(SignRequest signRequest) {
        postingSheetValidation(signRequest);

        if (fileIsNotExists(signRequest.getFilePathToP12()))
            throw new KeyStoreFileNotFoundException();

    }

    private void postingSheetValidation(SignRequest signRequest) {
        try{
            signRequest.getPostingSheet().validate();
        } catch (PostingSheet.InvalidPostingSheetException e){
            throw new PostingSheet.InvalidPostingSheetException(UserMessage.INVALID_POSTING_SHEET, e);
        }
    }

    private boolean fileIsNotExists(String pathToP12) {
        return !(new File(pathToP12).exists());
    }

    private void clearPassword(SignRequest signRequest) {
        char[] passwordToP12 = signRequest.getPasswordToP12();
        Arrays.fill(passwordToP12, '\0');
    }

    @Override
    public void notifyAllObservers(PostingSheet postingSheet) {
        for (ObserverInteractor observer : observers)
            observer.notifySigningDone(postingSheet);
    }

    @Override
    public void registerObserver(ObserverInteractor observer) {
        observers.add(observer);
    }
}
