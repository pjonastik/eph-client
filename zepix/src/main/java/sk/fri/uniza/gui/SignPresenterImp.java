package sk.fri.uniza.gui;

import sk.fri.uniza.*;
import sk.fri.uniza.FilePostingSheetFactory.FilePostingSheetException;
import sk.fri.uniza.Signer.KeyStoreAuthenticationException;
import sk.fri.uniza.MainView.CanceledFileChooserException;
import sk.fri.uniza.SignInteractor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;


public class SignPresenterImp implements SignPresenter {

    private MainView mainView;
    private SaveInteractor saveInteractor;
    private SendInteractor sendInteractor;

    public SignPresenterImp(MainView mainView, SignInteractor signInteractor, Signer signer,
                            SaveInteractor saveInteractor) {
        this.mainView = mainView;
        this.saveInteractor = saveInteractor;
        addSignActionListenerToView(mainView, signInteractor);
        addPolicyItemListener(mainView, signer);
        showSignaturePoliciesAndSignParameters(mainView, signer);
        addSaveActionListnerToView();
    }

    private void addSignActionListenerToView(MainView view, SignInteractor signInteractor) {
        view.addSignListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    protected Void doInBackground() throws Exception {
                        sign();
                        return null;
                    }
                }.execute();
            }

            private void sign() {
                try {
                    signInteractor.sign(createAndSetSignRequest());
                } catch (FilePostingSheetException | PostingSheet.InvalidPostingSheetException |
                        KeyStoreAuthenticationException | SignInteractor.KeyStoreFileNotFoundException ex) {

                    view.showError(ex.getMessage());
                }
            }

            private SignRequest createAndSetSignRequest() {
                SignRequest request = new SignRequest();
                createAndSetPostingSheet(request);
                try {
                    tryGetInputDataFromEventDispatchThread(request);
                } catch (InterruptedException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return request;
            }

            private void tryGetInputDataFromEventDispatchThread(final SignRequest request)
                    throws InterruptedException, InvocationTargetException {

                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        request.setSignaturePolicy(view.getSelectedPolicy());
                        request.setSignatureStructure(view.getSelectedStructure());
                        request.setSignatureLevel(view.getSelectedLevel());
                        request.setDigestAlgorithm(view.getSelectedDigestAlgorithm());
                        request.setFilePathToP12(view.getFilePathToKeyStore());
                        request.setPasswordToP12(view.getPasswordToKeyStore());
                    }
                });
            }

            private void createAndSetPostingSheet(SignRequest request) {
                String validInfoFilePath = view.getValidationInfoFilePath();
                PostingSheet ps = null;
                if (validInfoFilePath == null) {
                    ps = FilePostingSheetFactory.getInstance(view.getPostingSheetFilePath());
                } else {
                    ps = FilePostingSheetFactory.getInstance(view.getPostingSheetFilePath(), validInfoFilePath);
                }
                request.setPostingSheet(ps);
            }
        });
    }

    private void addPolicyItemListener(MainView mainView, Signer signer) {
        mainView.addPolicyListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED){
                    SignaturePolicy policy = (SignaturePolicy) e.getItem();
                    showSignatureParametersForSpecificPolicy(mainView, signer, policy);
                    mainView.setUnsignedState();
                }
            }
        });
    }

    private void showSignaturePoliciesAndSignParameters(MainView mainView, Signer signer) {
        Collection<SignaturePolicy> policies = signer.getSignaturePolicies();
        mainView.showPolicies(policies);
        showSignatureParametersForSpecificPolicy(mainView, signer, getFirstPolicy(policies));
    }

    private SignaturePolicy getFirstPolicy(Collection<SignaturePolicy> policies) {
        return policies.iterator().next();
    }

    private void showSignatureParametersForSpecificPolicy(MainView mainView, Signer signer, SignaturePolicy firstPolicy) {
        mainView.showDigestAlgorithms(signer.getDigestAlgorithms(firstPolicy));
        mainView.showStructures(signer.getSignatureStructures(firstPolicy));
        mainView.showLevels(signer.getSignatureLevels(firstPolicy));
    }

    private void addSaveActionListnerToView() {
        mainView.addSaveListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File path = mainView.getFilePathToSavePostingSheet();
                    executeSwingWorkerToSaveSignedPostingSheet(path);
                } catch (CanceledFileChooserException ignored) { return; }
            }

            private void executeSwingWorkerToSaveSignedPostingSheet(File path) {
                new SwingWorker<Void, Void>() {

                    protected Void doInBackground() throws Exception {
                        saveInteractor.save(path);
                        return null;
                    }
                }.execute();
            }
        });
    }

    private void addSendActionListenerToView() {
        mainView.addSendListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        send();
                        return null;
                    }
                }.execute();
            }

            private void send() {
                try {
                    sendInteractor.send(mainView.createSendRequest());
                }catch (SendInteractor.SendException ex){
                    mainView.showError(ex.getMessage());
                }
            }
        });
    }

    public void setSendInteractor(SendInteractor sendInteractor) {
        this.sendInteractor = sendInteractor;
        addSendActionListenerToView();
    }

    @Override
    public void tellPostingSheetSigned(SignResponse response) {
        mainView.setSignedState();
    }
}
