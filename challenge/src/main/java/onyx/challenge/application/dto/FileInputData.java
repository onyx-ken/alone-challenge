package onyx.challenge.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileInputData {

    private final String originalFilename;
    private final byte[] content;
    private final String contentType;

    public static FileInputData create(String originalFilename, byte[] content, String contentType) {
        return new FileInputData(originalFilename, content, contentType);
    }

    public static FileInputData convert(MultipartFile multipartFile) {
        try {
            return new FileInputData(
                    multipartFile.getOriginalFilename(),
                    multipartFile.getBytes(),
                    multipartFile.getContentType()
            );
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }
    }
}
