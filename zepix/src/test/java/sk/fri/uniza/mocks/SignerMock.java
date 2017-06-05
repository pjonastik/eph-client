package sk.fri.uniza.mocks;

import org.junit.Assert;
import sk.fri.uniza.SignaturePolicy;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;

public class SignerMock extends SignerSpy{
    SignaturePolicy expectedPolicy;

    int nrOfCalls_getDigestAlgorithms;
    int nrOfCalls_getSignatureStructures;
    int nrOfCalls_getSignatureLevels;
    int orderNumOfCallToVerify = 1;

    public SignerMock() {
        digestAlgs = Collections.emptyList();
        structures = Collections.emptyList();
        levels = Collections.emptyList();
    }

    @Override
    public Collection<DigestAlgorithm> getDigestAlgorithms(SignaturePolicy policy) {
        if(++nrOfCalls_getDigestAlgorithms == orderNumOfCallToVerify)
            Assert.assertThat(policy, is(expectedPolicy));

        return digestAlgs;
    }

    @Override
    public Collection<SignatureStructure> getSignatureStructures(SignaturePolicy policy) {
        if(++nrOfCalls_getSignatureStructures == orderNumOfCallToVerify)
            Assert.assertThat(policy, is(expectedPolicy));

        return structures;
    }

    @Override
    public Collection<SignatureLevel> getSignatureLevels(SignaturePolicy policy) {
        if(++nrOfCalls_getSignatureLevels == orderNumOfCallToVerify)
            Assert.assertThat(policy, is(expectedPolicy));

        return levels;
    }

    public void setExpectedPolicy(SignaturePolicy expectedPolicy) {
        this.expectedPolicy = expectedPolicy;
    }

    public void verifyThatSignParameterGettersWasInvokedWithExpectedPolicy() {
        Assert.assertThat(nrOfCalls_getDigestAlgorithms, is(orderNumOfCallToVerify));
        Assert.assertThat(nrOfCalls_getSignatureStructures, is(orderNumOfCallToVerify));
        Assert.assertThat(nrOfCalls_getSignatureLevels, is(orderNumOfCallToVerify));
    }

    public void setOrderNumOfCallToVerify(int orderNumOfCallToVerify) {
        this.orderNumOfCallToVerify = orderNumOfCallToVerify;
    }


}
