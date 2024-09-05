package onyx.file;

import lombok.extern.slf4j.Slf4j;
import onyx.file.domain.FileInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class LocalFileStorageStrategy implements FileStorageStrategy {

    @Override
    public FileInfo saveFile(File file, String uploadDir, String fileName) throws IOException {
        Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(fileStorageLocation); // 디렉토리 생성
        Path targetLocation = fileStorageLocation.resolve(fileName);

        Files.copy(file.toPath(), targetLocation); // 파일 저장

        return FileInfo.create(targetLocation.toString(), file.getName(),
                fileName, file.length(), Files.probeContentType(file.toPath()));
    }

    @Override
    public void deleteFile(String uploadPath) {
        Path path = Paths.get(uploadPath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("파일 삭제 실패", e);
        }
    }

    @Override
    public FileInfo getFileInfo(String uploadDir, String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + fileName);
        }

        return FileInfo.create(filePath.toString(), fileName, file.getName(),
                file.length(), Files.probeContentType(filePath));
    }
}
