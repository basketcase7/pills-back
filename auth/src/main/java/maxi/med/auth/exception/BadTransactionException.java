package maxi.med.auth.exception;

public class BadTransactionException extends RuntimeException {
    public BadTransactionException(String message) {
        super(message);
    }
}
