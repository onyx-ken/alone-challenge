package onyx.file.service;

import onyx.file.domain.FileInfo;

import java.util.Optional;

public interface FileService {
    Optional<FileInfo> getFileInfoById(Long fileId);
}
