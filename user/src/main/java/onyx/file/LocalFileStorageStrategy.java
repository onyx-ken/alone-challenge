package onyx.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileStorageStrategy implements FileStorageStrategy {

    @Value("${storage.path}")
    private String rootUploadDir;

    @Override
    public String saveFile(MultipartFile file, String subDir, String fileName) throws IOException {
        // rootUploadDir 경로 아래의 서브 디렉토리를 포함한 경로 생성
        Path fileStorageLocation = Paths.get(rootUploadDir, subDir).toAbsolutePath().normalize();
        Path targetLocation = fileStorageLocation.resolve(fileName);

        Files.createDirectories(fileStorageLocation); // 디렉토리 생성
        file.transferTo(targetLocation.toFile());

        return targetLocation.toString();
    }

    @Override
    public void deleteFile(String subDir, String fileUrl) {
        Path fileLocation = Paths.get(rootUploadDir, subDir).toAbsolutePath().normalize().resolve(fileUrl);
        File file = fileLocation.toFile();
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public String getFileUrl(String subDir, String fileName) {
        Path fileStorageLocation = Paths.get(rootUploadDir, subDir).toAbsolutePath().normalize();
        return fileStorageLocation.resolve(fileName).toString();
    }
}
