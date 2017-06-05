package main;

import sk.fri.uniza.*;
import sk.fri.uniza.gui.SignPresenterImp;
import sk.fri.uniza.gui.SwingMainView;
import sk.fri.uniza.save.SaveInteractorImp;
import sk.fri.uniza.send.RESTSender;
import sk.fri.uniza.send.SendInteractorImp;
import sk.fri.uniza.sign.DSSSignerAdapter;
import sk.fri.uniza.sign.DSSXAdESSigner;
import sk.fri.uniza.sign.SignInteractorImp;
import sk.fri.uniza.sign.SignaturePolicyImp;
import sk.fri.uniza.utils.DateUtil;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;

import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.invokeLater;

public class Main {
    static SwingMainView signView;

    public static void main(String[] args) {
        DSSSignerAdapter signer = new DSSSignerAdapter(new DSSXAdESSigner());
        signer.setSignaturePolicies(readAndValidateSlovakPolicies());
        SignInteractorImp signInteractor = new SignInteractorImp();
        signInteractor.setSigner(signer);

        SendInteractorImp sendInteractorImp = new SendInteractorImp(signInteractor);
        sendInteractorImp.setSender(new RESTSender());

        createSignViewGUI();
        SignPresenterImp presenter = new SignPresenterImp(signView, signInteractor, signer,
                new SaveInteractorImp(signInteractor));
        presenter.setSendInteractor(sendInteractorImp);
        signInteractor.setPresenter(presenter);
        showGUI();
    }

    private static void createSignViewGUI() {
        try {
            tryInvokeCreationOfGUIAndWait();
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void tryInvokeCreationOfGUIAndWait() throws InterruptedException, InvocationTargetException {
        invokeAndWait(new Runnable() {
            @Override
            public void run() {
                signView = createSignView();
            }
        });
    }

    private static void showGUI() {
        invokeLater(new Runnable() {
            public void run() {
                signView.setVisible(true);
            }
        });
    }

    private static Collection<SignaturePolicy> readAndValidateSlovakPolicies() {
        SignaturePolicyImp nbuPolicy = simulateReadPolicy();
        try {
            nbuPolicy.validate();
        } catch (SignaturePolicy.InvalidSignaturePolicyException  e){
            //if invalid, notify user
        }
        return Collections.singleton(nbuPolicy);
    }

    /**
     * This is only simulation. We should read signature policy from DER file!
     */
    private static SignaturePolicyImp simulateReadPolicy() {
        SignaturePolicyImp nbuPolicy = new SignaturePolicyImp();
        nbuPolicy.setOID("1.3.158.36061701.1.2.2");
        nbuPolicy.setDigestAlgorithm(Signer.DigestAlgorithm.SHA256);
        nbuPolicy.setDigestValue("C876D4CA4A875295C838244A40B422CC9ECA3BB397E0E0E6CB8D6242F37F8A9F".getBytes());
        nbuPolicy.setComeIntoForceDate(DateUtil.parseDate("2016-10-02"));
        nbuPolicy.setExpirationDate(DateUtil.parseDate("2021-10-02"));
        return nbuPolicy;
    }

    private static SwingMainView createSignView() {
        installSystemLookAndFeel();
        return new SwingMainView();
    }

    private static void installSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
