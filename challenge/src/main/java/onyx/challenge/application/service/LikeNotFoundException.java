package onyx.challenge.application.service;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException(String message) {
        super(message);
    }
}
