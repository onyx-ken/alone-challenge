package onyx.challenge.application.service.exceptiron.file;

public class FileEmptyException extends StorageException {
    public FileEmptyException() {
        super("storage.empty.file");
    }
}
