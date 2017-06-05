package sk.fri.uniza.sign;

import sk.fri.uniza.SignaturePolicy;
import sk.fri.uniza.Signer.DigestAlgorithm;

import static sk.fri.uniza.SignaturePolicy.InvalidSignaturePolicyException.MALFORMED_DATES_MSG;
import static sk.fri.uniza.SignaturePolicy.InvalidSignaturePolicyException.TODAY_DATE_OUT_OF_SIGN_PERIOD_MSG;

import java.util.Date;

public class SignaturePolicyImp implements SignaturePolicy {
    private Date comeIntoForceDate;
    private Date expirationDate;
    private String oid;
    private DigestAlgorithm digestAlgorithm;
    private byte[] digestValue;

    public void setComeIntoForceDate(Date comeIntoForceDate) {
        this.comeIntoForceDate = comeIntoForceDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public void validate() {
        if(isComeIntoForceDateGtThanExpiredDate())
            throw new InvalidSignaturePolicyException(MALFORMED_DATES_MSG);
        throwExceptionIfTodayDateIsOutOfSingingPeriodOfSignPolicy();
    }

    @Override
    public String getOID() {
        return oid;
    }

    @Override
    public DigestAlgorithm getDigestAlgorithm() {
        return digestAlgorithm;
    }

    @Override
    public byte[] getDigestValue() {
        return digestValue;
    }

    private boolean isComeIntoForceDateGtThanExpiredDate() {
        return comeIntoForceDate.after(expirationDate);
    }

    private void throwExceptionIfTodayDateIsOutOfSingingPeriodOfSignPolicy() {
        if(todaysDateIsOutOfSingingPeriodOfSignPolicy()){
            String m = String.format(TODAY_DATE_OUT_OF_SIGN_PERIOD_MSG, oid, comeIntoForceDate, expirationDate);
            throw new SignaturePolicy.InvalidSignaturePolicyException(m);
        }
    }

    private boolean todaysDateIsOutOfSingingPeriodOfSignPolicy() {
        Date today = new Date();
        return today.before(comeIntoForceDate) || today.after(expirationDate);
    }

    public void setOID(String oid) {
        this.oid = oid;
    }

    @Override
    public String getName() {
        return getOID();
    }


    public void setDigestAlgorithm(DigestAlgorithm digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public void setDigestValue(byte[] digestValue) {
        this.digestValue = digestValue;
    }
}
