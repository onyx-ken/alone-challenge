package onyx.challenge.framework.adapter.outbound.file;

import lombok.Setter;
import onyx.challenge.application.port.outbound.FileStorage;
import onyx.challenge.application.service.StorageException;
import onyx.challenge.application.service.StorageFileNotFoundException;
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
                throw new StorageException("빈 파일은 저장할 수 없습니다.");
            }
            Path destinationFile = rootLocation.resolve(storedFilename).normalize().toAbsolutePath();

            Files.createDirectories(destinationFile.getParent());
            Files.write(destinationFile, data);

            return destinationFile.toString();
        } catch (IOException e) {
            throw new StorageException("파일 저장 실패: " + storedFilename, e);
        }
    }

    @Override
    public byte[] load(String storedFilename) {
        try {
            Path filePath = rootLocation.resolve(storedFilename).normalize().toAbsolutePath();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new StorageFileNotFoundException("파일 로드 실패: " + storedFilename, e);
        }
    }

    @Override
    public void delete(String storedFilename) {
        try {
            Path filePath = rootLocation.resolve(storedFilename).normalize().toAbsolutePath();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new StorageException("파일 삭제 실패: " + storedFilename, e);
        }
    }
}
