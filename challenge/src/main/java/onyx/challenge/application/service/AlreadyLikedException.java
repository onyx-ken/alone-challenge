package onyx.challenge.application.service;

public class AlreadyLikedException extends RuntimeException {

    public AlreadyLikedException() {
        super();
    }

    public AlreadyLikedException(String message) {
        super(message);
    }

    public AlreadyLikedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyLikedException(Throwable cause) {
        super(cause);
    }
}
