package onyx.file.service;

import lombok.RequiredArgsConstructor;
import onyx.file.domain.FileInfo;
import onyx.file.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public Optional<FileInfo> getFileInfoById(Long fileId) {
        return fileRepository.findById(fileId);
    }
}
