package onyx.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageStrategy {
    String saveFile(MultipartFile file, String subDir, String fileName) throws IOException;
    void deleteFile(String subDir, String fileUrl);
    String getFileUrl(String subDir, String fileName);
}
