package onyx.challenge.application.service.exceptiron.file;

public abstract class StorageException extends RuntimeException {

    private final String messageCode;
    private final Object[] args;

    public StorageException(String messageCode, Object... args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public Object[] getArgs() {
        return args;
    }
}
