package onyx.challenge.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileData {

    private final String originalFilename;
    private final byte[] content;
    private final String contentType;

    public static FileData create(String originalFilename, byte[] content, String contentType) {
        return new FileData(originalFilename, content, contentType);
    }

    public static FileData convert(MultipartFile multipartFile) {
        try {
            return new FileData(
                    multipartFile.getOriginalFilename(),
                    multipartFile.getBytes(),
                    multipartFile.getContentType()
            );
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }
    }
}
