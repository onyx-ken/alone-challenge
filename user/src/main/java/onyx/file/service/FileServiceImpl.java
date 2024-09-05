package onyx.file.service;

import lombok.RequiredArgsConstructor;
import onyx.file.FileStorageStrategy;
import onyx.file.domain.FileInfo;
import onyx.file.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileStorageStrategy fileStorageStrategy;

    @Transactional
    @Override
    public FileInfo saveFile(File file, String uploadDir, String fileName) throws IOException {
        return fileRepository.save(fileStorageStrategy.saveFile(file, uploadDir, fileName));
    }

    @Transactional
    @Override
    public void deleteFileByFileId(Long fileId) {
        getFileInfoById(fileId).ifPresent(file -> {
            fileStorageStrategy.deleteFile(file.getPath());
            file.setUseYn(false);
        });
    }

    @Override
    public Optional<FileInfo> getFileInfoById(Long fileId) {
        return fileRepository.findById(fileId);
    }
}
