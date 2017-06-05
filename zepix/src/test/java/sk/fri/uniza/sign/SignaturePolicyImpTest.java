package sk.fri.uniza.sign;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sk.fri.uniza.SignaturePolicy;
import sk.fri.uniza.sign.SignaturePolicyImp;

import java.util.Calendar;
import java.util.Date;

import static sk.fri.uniza.SignaturePolicy.InvalidSignaturePolicyException;
import static sk.fri.uniza.SignaturePolicy.InvalidSignaturePolicyException.MALFORMED_DATES_MSG;
import static sk.fri.uniza.SignaturePolicy.InvalidSignaturePolicyException.*;

public class SignaturePolicyImpTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private SignaturePolicyImp policy;

    @Before
    public void setUp() throws Exception {
        policy = new SignaturePolicyImp();
    }

    @Test
    public void implementsSignaturePolicy() throws Exception {
        SignaturePolicy p = policy;
    }

    private Date yesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    private Date today() {
        return new Date();
    }

    private Date tomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,
                +1);
        return calendar.getTime();
    }

    private void expectExceptionWithMessage(Class<InvalidSignaturePolicyException> exceptionClass, String message) {
        expectedException.expect(exceptionClass);
        expectedException.expectMessage(message);
    }

    @Test
    public void validateShouldFail_WrongPolicyDates() throws Exception {
        policy.setComeIntoForceDate(tomorrow());
        policy.setExpirationDate(yesterday());

        expectExceptionWithMessage(InvalidSignaturePolicyException.class, MALFORMED_DATES_MSG);
        policy.validate();
    }

    @Test
    public void signShouldFailed_ExpirationDateOfSignPolicyIsLessThanToday() throws Exception {
        Date comeIntoForceDate, expirationDate;
        comeIntoForceDate = expirationDate = yesterday();
        String oid = "oid";
        policy.setOID(oid);
        policy.setComeIntoForceDate(comeIntoForceDate);
        policy.setExpirationDate(expirationDate);
        String m = String.format(TODAY_DATE_OUT_OF_SIGN_PERIOD_MSG, oid, comeIntoForceDate, expirationDate);
        expectExceptionWithMessage(InvalidSignaturePolicyException.class, m);
        policy.validate();
    }


    @Test
    public void signShouldFailed_ComeIntoForceDateOfSignPolicyIsGtThanToday() throws Exception {
        Date comeIntoForceDate, expirationDate;
        comeIntoForceDate = expirationDate = tomorrow();
        String oid = "oid";
        policy.setOID(oid);
        policy.setComeIntoForceDate(comeIntoForceDate);
        policy.setExpirationDate(expirationDate);
        String m = String.format(TODAY_DATE_OUT_OF_SIGN_PERIOD_MSG, oid, comeIntoForceDate, expirationDate);
        expectExceptionWithMessage(InvalidSignaturePolicyException.class, m);
        policy.validate();
    }

    @Test
    public void signShouldPass_WhenExpirationDateOfSignPolicyIsGtThanToday() throws Exception {
        policy.setComeIntoForceDate(today());
        policy.setExpirationDate(tomorrow());
        policy.validate();
    }

    @Test
    public void signShouldPass_WhenComeIntoForceDateOfSignPolicyIsLessThanToday() throws Exception {
        policy.setComeIntoForceDate(yesterday());
        policy.setExpirationDate(tomorrow());
        policy.validate();
    }

    @Test
    public void signShouldPass_WhenExpirationOrComeIntoForceDateAreEqualToToday() throws Exception {
        Date comeIntoForceDate, expirationDate;
        comeIntoForceDate = expirationDate = today();
        policy.setComeIntoForceDate(comeIntoForceDate);
        policy.setExpirationDate(expirationDate);
        policy.validate();
    }
}
