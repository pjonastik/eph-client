package sk.fri.uniza.gui;

import sk.fri.uniza.SignaturePolicy;
import sk.fri.uniza.Signer.DigestAlgorithm;
import sk.fri.uniza.Signer.SignatureLevel;
import sk.fri.uniza.Signer.SignatureStructure;

import java.awt.event.ActionListener;
import java.util.Collection;

public class MainViewSpy extends MainViewStub {

    private Collection<SignaturePolicy> policies;
    private Collection<DigestAlgorithm> shownDigestAlgorithms;
    private Collection<SignatureStructure> shownStructures;
    private Collection<SignatureLevel> shownLevels;

    private boolean setSignedStateInvoked;

    private boolean showErrorInvoked;
    private String spiedErrorMessage;

    private boolean clearSignedPostingSheetViewStateInvoked;

    private ActionListener spiedRegisteredSignListener;
    private ActionListener spiedRegistredSaveListener;
    private ActionListener spiedRegistredSendListener;

    @Override
    public void showPolicies(Collection<SignaturePolicy> policies) {
        this.policies = policies;
    }

    Collection<SignaturePolicy> getShownPolicies() {
        return policies;
    }

    @Override
    public void showDigestAlgorithms(Collection<DigestAlgorithm> digestAlgorithms) {
        this.shownDigestAlgorithms = digestAlgorithms;
    }

    Collection<DigestAlgorithm> getShownDigestAlgorithms() {
        return shownDigestAlgorithms;
    }

    @Override
    public void showStructures(Collection<SignatureStructure> signatureStructures) {
        this.shownStructures = signatureStructures;
    }

    Collection<SignatureStructure> getShownStructures() {
        return shownStructures;
    }

    @Override
    public void showLevels(Collection<SignatureLevel> signatureLevels) {
        this.shownLevels = signatureLevels;
    }

    Collection<SignatureLevel> getShownLevels() {
        return shownLevels;
    }

    void resetShownSignatureParameters() {
        shownDigestAlgorithms = null;
        shownStructures = null;
        shownLevels = null;
    }

    @Override
    public void showError(String message) {
        showErrorInvoked = true;
        spiedErrorMessage = message;
    }

    public boolean wasShowErrorInvoked() {
        return showErrorInvoked;
    }

    @Override
    public void setSignedState() {
        setSignedStateInvoked = true;
    }

    @Override
    public void setUnsignedState() {
        clearSignedPostingSheetViewStateInvoked = true;
    }

    boolean wasClearSignedPostingSheetViewStateInvoked() {
        return clearSignedPostingSheetViewStateInvoked;
    }

    String getSpiedErrorMessage() {
        return spiedErrorMessage;
    }

    public boolean wasSetSignedStateInvoked() {
        return setSignedStateInvoked;
    }

    @Override
    public void addSignListener(ActionListener actionListener) {
        spiedRegisteredSignListener = actionListener;
    }

    public ActionListener getSpiedRegisteredSignListener() {
        return spiedRegisteredSignListener;
    }

    @Override
    public void addSaveListener(ActionListener actionListener) {
        spiedRegistredSaveListener = actionListener;
    }

    public ActionListener getSpiedRegistredSaveListener() {
        return spiedRegistredSaveListener;
    }

    @Override
    public void addSendListener(ActionListener actionListener) {
        spiedRegistredSendListener = actionListener;
    }

    public ActionListener getSpiedRegistredSendListener() {
        return spiedRegistredSendListener;
    }
}
