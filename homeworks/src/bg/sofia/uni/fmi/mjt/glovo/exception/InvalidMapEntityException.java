package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidMapEntityException extends IllegalArgumentException {
    public InvalidMapEntityException(String message) {
        super(message);
    }

    public InvalidMapEntityException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
