package sk.fri.uniza;


import sk.fri.uniza.utils.UserMessage;

public interface SignInteractor {
    void sign(SignRequest signRequest);

    class KeyStoreFileNotFoundException extends RuntimeException {
        public KeyStoreFileNotFoundException() {
            super(UserMessage.KEYSTORE_FILE_NOT_FOUND);
        }
    }
}
