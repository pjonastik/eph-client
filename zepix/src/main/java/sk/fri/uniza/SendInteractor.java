package sk.fri.uniza;

public interface SendInteractor {
    void send(SendRequest sendRequest);

    class SendException extends RuntimeException{
        public SendException(String msg) {
            super(msg);
        }
    }
}
