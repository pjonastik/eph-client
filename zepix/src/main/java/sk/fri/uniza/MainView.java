package sk.fri.uniza;

import sk.fri.uniza.Signer.DigestAlgorithm;
import sk.fri.uniza.Signer.SignatureLevel;
import sk.fri.uniza.Signer.SignatureStructure;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Collection;

public interface MainView {
    ////////////////////////////
    // methods for controller //
    ////////////////////////////
    void addPolicyListener(ItemListener itemListener);

    void addSignListener(ActionListener actionListener);

    void addSaveListener(ActionListener actionListener);

    void addSendListener(ActionListener actionListener);

    String getPostingSheetFilePath();

    String getValidationInfoFilePath();

    SignaturePolicy getSelectedPolicy();

    SignatureStructure getSelectedStructure();

    SignatureLevel getSelectedLevel();

    DigestAlgorithm getSelectedDigestAlgorithm();

    String getFilePathToKeyStore();

    char [] getPasswordToKeyStore();

    File getFilePathToSavePostingSheet();

    SendRequest createSendRequest();

    ///////////////////////////
    // methods for presenter //
    ///////////////////////////

    /**
     * We assume that first element in collection will be shown as selected by default
     * */
    void showPolicies(Collection<SignaturePolicy> signaturePolicies);

    void showDigestAlgorithms(Collection<DigestAlgorithm> digestAlgorithms);

    void showStructures(Collection<SignatureStructure> signatureStructures);

    void showLevels(Collection<SignatureLevel> signatureLevels);

    void showError(String message);

    void setUnsignedState();

    void setSignedState();


    class CanceledFileChooserException extends RuntimeException {
    }
}
