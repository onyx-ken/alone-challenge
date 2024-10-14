package onyx.challenge.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileData {

    private final String originalFilename;
    private final byte[] content;
    private final String contentType;

    public static FileData create(String originalFilename, byte[] content, String contentType) {
        return new FileData(originalFilename, content, contentType);
    }
}
