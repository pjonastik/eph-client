package sk.fri.uniza.gui;

import sk.fri.uniza.SignaturePolicy;
import sk.fri.uniza.sign.SignaturePolicyImp;
import sk.fri.uniza.Signer;

import java.util.Arrays;
import java.util.Collection;

public class SignViewManualTest {

    public static void main(String[] args) {

        SwingMainView signView = new SwingMainView();

        signView.showPolicies(createPolicies());
        signView.showStructures(createStructures());
        signView.showLevels(createLevels());
        signView.showDigestAlgorithms(createDigestAlgorithms());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                signView.setVisible(true);
            }
        });
    }

    private static Collection<Signer.DigestAlgorithm> createDigestAlgorithms() {
        return Arrays.asList(Signer.DigestAlgorithm.SHA1, Signer.DigestAlgorithm.SHA256);
    }

    private static Collection<Signer.SignatureLevel> createLevels() {
        return Arrays.asList(Signer.SignatureLevel.B, Signer.SignatureLevel.T);
    }

    private static Collection<Signer.SignatureStructure> createStructures() {
        return Arrays.asList(Signer.SignatureStructure.ENVELOPED, Signer.SignatureStructure.ENVELOPING);
    }

    private static Collection<SignaturePolicy> createPolicies() {
        SignaturePolicyImp p1 = new SignaturePolicyImp();
        p1.setOID("Policy 1");
        SignaturePolicyImp p2 = new SignaturePolicyImp();
        p2.setOID("Policy 2");
        return Arrays.asList(p1, p2);
    }
}
