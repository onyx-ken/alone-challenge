package onyx.file;

import onyx.file.domain.FileInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public void deleteFile(String uploadDir, String fileName) {
        File file = new File(uploadDir + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public FileInfo getFileInfo(String uploadDir, String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        return FileInfo.create(filePath.toString(), fileName, file.getName(),
                file.length(), Files.probeContentType(filePath));
    }
}
