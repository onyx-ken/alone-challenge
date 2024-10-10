package onyx.challenge.application.service.exceptiron.common;

public abstract class AlreadyExistsException extends DomainException {
    protected AlreadyExistsException(String messageCode, Object... args) {
        super(messageCode, args);
    }
}
