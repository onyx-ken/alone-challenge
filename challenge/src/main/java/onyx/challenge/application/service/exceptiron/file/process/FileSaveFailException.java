package onyx.challenge.application.service.exceptiron.file.process;

public class FileSaveFailException extends FileProcessingException {
    public FileSaveFailException(String storedFilename) {
        super("storage.save.failed", storedFilename);
    }
}
