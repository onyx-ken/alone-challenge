package onyx.challenge.application.service.exceptiron.file;

public class FileNotFoundException extends StorageException {
    public FileNotFoundException(String storedFilename) {
        super("storage.load.failed", storedFilename);
    }
}
