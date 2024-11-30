package bg.sofia.uni.fmi.mjt.glovo.exception;

public class UnreachableClientException extends RuntimeException {
    public UnreachableClientException(String message) {
        super(message);
    }

    public UnreachableClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
