package sk.fri.uniza;

import sk.fri.uniza.utils.UserMessage;

import java.util.Collection;

public interface Signer {

    Collection<SignaturePolicy> getSignaturePolicies();

    Collection<DigestAlgorithm> getDigestAlgorithms(SignaturePolicy policy);

    Collection<SignatureStructure> getSignatureStructures(SignaturePolicy policy);

    Collection<SignatureLevel> getSignatureLevels(SignaturePolicy policy);

    PostingSheet signPostingSheet(SignRequest parameters) throws KeyStoreAuthenticationException;

    enum SignatureLevel implements Identifiable{
        B("XAdES Baseline B-Level"), T("XAdES Baseline T-Level"), NONE("None signature level");

        private String name;
        SignatureLevel( String name) {
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    enum SignatureStructure implements Identifiable{
        ENVELOPING("ENVELOPING"), ENVELOPED("ENVELOPED"), NONE("None signature structure");

        private String name;
        SignatureStructure (String name) {
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    enum DigestAlgorithm implements Identifiable{
        SHA256("SHA256"), SHA1("SHA-1"), NONE("None digest algorithm");
        private String name;
        DigestAlgorithm (String name) {
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    class InitException extends RuntimeException{
        public InitException(String s) {
            super(s);
        }
    }

    class KeyStoreAuthenticationException extends RuntimeException {
        public KeyStoreAuthenticationException(Throwable cause) {
            super(UserMessage.AUTHENTICATION_KEYSTORE_FAILED, cause);
        }
    }
}
