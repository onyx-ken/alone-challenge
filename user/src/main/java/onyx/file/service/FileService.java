package onyx.file.service;

import onyx.file.domain.FileInfo;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface FileService {
    FileInfo saveFile(File file, String uploadDir, String fileName) throws IOException;
    void deleteFileByFileId(Long fileId);
    Optional<FileInfo> getFileInfoById(Long fileId);
}
