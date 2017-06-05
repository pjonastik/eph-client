package sk.fri.uniza.gui;

import sk.fri.uniza.MainView;
import sk.fri.uniza.testUtils.TestResourceUtil;
import sk.fri.uniza.SendRequest;
import sk.fri.uniza.testUtils.SendRequestHelper;
import sk.fri.uniza.SignaturePolicy;
import sk.fri.uniza.Signer.DigestAlgorithm;
import sk.fri.uniza.Signer.SignatureLevel;
import sk.fri.uniza.Signer.SignatureStructure;
import sk.fri.uniza.mocks.SignaturePolicyDummy;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Collection;

public class MainViewStub implements MainView {


    private ItemListener registeredPolicyListener;
    private String postingSheetFilePath = TestResourceUtil.VALID_XML_FILEPATH;
    private String xsdFilePath = TestResourceUtil.VALID_XSD_FILEPATH;

    private SignaturePolicy selectedSingaturePolicy = new SignaturePolicyDummy();
    private SignatureStructure selectedSignatureStructure = SignatureStructure.NONE;
    private SignatureLevel selectedLevel = SignatureLevel.NONE;
    private DigestAlgorithm selectedDigestAlg = DigestAlgorithm.NONE;

    private String filePathToKeyStore = "path/to/keyStore";
    private char[] passwordToKeyStore = "password to keyStore".toCharArray();

    private File filePathForSignedPostingSheet;

    public MainViewStub() {

    }

    public ItemListener getRegisteredPolicyListener() {
        return registeredPolicyListener;
    }

    @Override
    public void addPolicyListener(ItemListener itemListener) {
        registeredPolicyListener = itemListener;
    }

    @Override
    public void addSignListener(ActionListener actionListener) {

    }

    @Override
    public void addSaveListener(ActionListener actionListener) {

    }

    @Override
    public void addSendListener(ActionListener actionListener) {

    }

    @Override
    public void showPolicies(Collection<SignaturePolicy> signaturePolicies) {
    }

    @Override
    public void showDigestAlgorithms(Collection<DigestAlgorithm> digestAlgorithms) {
    }

    @Override
    public void showStructures(Collection<SignatureStructure> signatureStructures) {
    }

    @Override
    public void showLevels(Collection<SignatureLevel> signatureLevels) {
    }

    @Override
    public String getPostingSheetFilePath() {
        return postingSheetFilePath;
    }

    public void setPostingSheetFilePath(String postingSheetfilePath) {
        this.postingSheetFilePath = postingSheetfilePath;
    }

    @Override
    public void showError(String message) {
    }

    @Override
    public String getValidationInfoFilePath() {
        return xsdFilePath;
    }

    public void setValidationInfoFilePath(String validInfoFilePath) {
        this.xsdFilePath = validInfoFilePath;
    }

    @Override
    public SignaturePolicy getSelectedPolicy() {
        return selectedSingaturePolicy;
    }

    @Override
    public SignatureStructure getSelectedStructure() {
        return selectedSignatureStructure;
    }

    @Override
    public SignatureLevel getSelectedLevel() {
        return selectedLevel;
    }

    @Override
    public DigestAlgorithm getSelectedDigestAlgorithm() {
        return selectedDigestAlg;
    }

    @Override
    public String getFilePathToKeyStore() {
        return filePathToKeyStore;
    }

    @Override
    public char[] getPasswordToKeyStore() {
        return passwordToKeyStore;
    }

    @Override
    public void setSignedState() {
    }

    @Override
    public File getFilePathToSavePostingSheet() {
        if (filePathForSignedPostingSheet == null)
            throw new MainView.CanceledFileChooserException();
        return filePathForSignedPostingSheet;
    }

    public void setFilePathForSignedPostingSheet(File path) {
        this.filePathForSignedPostingSheet = path;
    }

    @Override
    public SendRequest createSendRequest() {
        return SendRequestHelper.getInstance();
    }

    @Override
    public void setUnsignedState() {

    }



}
