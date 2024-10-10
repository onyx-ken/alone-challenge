package onyx.challenge.application.service.exceptiron.common;

public abstract class DomainException extends RuntimeException {

    private final String messageCode;
    private final Object[] args;

    protected DomainException(String messageCode, Object... args) {
        super(messageCode); // 메시지 코드를 예외 메시지로 설정
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
