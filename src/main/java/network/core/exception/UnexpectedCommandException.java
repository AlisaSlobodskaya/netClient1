package network.core.exception;

public class UnexpectedCommandException extends RuntimeException {
    public UnexpectedCommandException(String message) {
        super(message);
    }
}
