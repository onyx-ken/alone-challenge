package onyx.challenge.application.service.exceptiron.common;

public abstract class NotFoundException extends DomainException {
    protected NotFoundException(String messageCode, Object... args) {
        super(messageCode, args);
    }
}
