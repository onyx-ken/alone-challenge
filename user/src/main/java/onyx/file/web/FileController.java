package onyx.file.web;

import lombok.RequiredArgsConstructor;
import onyx.file.domain.FileInfo;
import onyx.file.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/users/files/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long fileId) {
        Optional<FileInfo> foundFile = fileService.getFileInfoById(fileId);

        if (foundFile.isPresent()) {
            // 파일을 Resource로 변환
            FileInfo fileInfo = foundFile.get();
            Path filePath = Paths.get(fileInfo.getPath());
            Resource resource;
            try {
                resource = new UrlResource(filePath.toUri());
            } catch (MalformedURLException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileInfo.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileInfo.getOriginalName() + "\"")
                    .body(resource);
        }

        return ResponseEntity.notFound().build();

    }
}
