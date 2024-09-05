package onyx.file;

import onyx.file.domain.FileInfo;

import java.io.File;
import java.io.IOException;

public interface FileStorageStrategy {
    FileInfo saveFile(File file, String uploadDir, String fileName) throws IOException;
    void deleteFile(String uploadPath);
    FileInfo getFileInfo(String uploadDir, String fileName) throws IOException;
}
