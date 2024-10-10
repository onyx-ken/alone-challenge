package onyx.challenge.framework.adapter.outbound.file;

import lombok.Setter;
import onyx.challenge.application.port.outbound.FileStorage;
import onyx.challenge.application.service.exceptiron.file.FileEmptyException;
import onyx.challenge.application.service.exceptiron.file.FileNotFoundException;
import onyx.challenge.application.service.exceptiron.file.process.FileDeleteFailException;
import onyx.challenge.application.service.exceptiron.file.process.FileSaveFailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
@Setter
public class FileSystemStorage implements FileStorage {

    private Path rootLocation;

    public FileSystemStorage(@Value("${storage.path.challenge}") String path) {
        this.rootLocation = Path.of(path);
    }

    @Override
    public String store(byte[] data, String storedFilename) {
        try {
            if (data.length == 0) {
                throw new FileEmptyException();
            }
            Path destinationFile = rootLocation.resolve(storedFilename).normalize().toAbsolutePath();

            Files.createDirectories(destinationFile.getParent());
            Files.write(destinationFile, data);

            return destinationFile.toString();
        } catch (IOException e) {
            throw new FileSaveFailException(storedFilename);
        }
    }

    @Override
    public byte[] load(String storedFilename) {
        try {
            Path filePath = rootLocation.resolve(storedFilename).normalize().toAbsolutePath();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new FileNotFoundException(storedFilename);
        }
    }

    @Override
    public void delete(String storedFilename) {
        try {
            Path filePath = rootLocation.resolve(storedFilename).normalize().toAbsolutePath();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileDeleteFailException(storedFilename);
        }
    }
}
