package sk.fri.uniza;

import static sk.fri.uniza.Signer.*;

public interface SignaturePolicy extends Identifiable{
    void validate();

    String getOID();

    DigestAlgorithm getDigestAlgorithm();

    byte[] getDigestValue();

    class InvalidSignaturePolicyException extends RuntimeException {
        public static final String MALFORMED_DATES_MSG = "malformed dates";
        public static final String TODAY_DATE_OUT_OF_SIGN_PERIOD_MSG = "Today's date is out of bound of singing period"+
                " of selected signature policy [oid: %s, from: %s to: %s]";
        public InvalidSignaturePolicyException(String message) {
            super(message);
        }
    }
}
