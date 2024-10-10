package onyx.challenge.application.service.exceptiron.file.process;

public class FileDeleteFailException extends FileProcessingException {
    public FileDeleteFailException(String storedFilename) {
        super("storage.delete.failed", storedFilename);
    }
}
