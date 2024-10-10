package onyx.challenge.application.service.exceptiron.file.process;

import onyx.challenge.application.service.exceptiron.file.StorageException;

public abstract class FileProcessingException extends StorageException {
    public FileProcessingException(String messageCode, Object... args) {
        super(messageCode, args);
    }
}
