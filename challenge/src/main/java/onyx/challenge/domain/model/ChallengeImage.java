package onyx.challenge.domain.model;

import lombok.Getter;

import java.util.List;

@Getter
public class ChallengeImage {

    private final Long id;                 // 이미지 식별자
    private final String originalFilename; // 원본 파일명
    private final String storedFilename;   // 저장된 파일명 (UUID 등)
    private final String filePath;         // 파일 경로
    private final long fileSize;           // 파일 크기
    private final String contentType;      // MIME 타입

    private static final int MAX_FILENAME_LENGTH = 255;
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/gif");

    public static ChallengeImage create(Long id, String originalFilename, String storedFilename,
                                        String filePath, long fileSize, String contentType) {
        return new ChallengeImage(id, originalFilename, storedFilename, filePath, fileSize, contentType);
    }

    public static ChallengeImage from(Long id, String originalFilename, String storedFilename,
                                      String filePath, long fileSize, String contentType) {
        return new ChallengeImage(id, originalFilename, storedFilename, filePath, fileSize, contentType);
    }

    private ChallengeImage(Long id, String originalFilename, String storedFilename,
                           String filePath, long fileSize, String contentType) {
        validateRequiredFields(originalFilename, storedFilename, filePath, fileSize, contentType);
        this.id = id;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.contentType = contentType;
    }

    private void validateRequiredFields(String originalFilename, String storedFilename,
                                        String filePath, long fileSize, String contentType) {
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("원본 파일명은 필수입니다.");
        }
        if (originalFilename.length() > MAX_FILENAME_LENGTH) {
            throw new IllegalArgumentException("원본 파일명의 길이는 " + MAX_FILENAME_LENGTH + "자를 초과할 수 없습니다.");
        }
        if (storedFilename == null || storedFilename.isBlank()) {
            throw new IllegalArgumentException("저장된 파일명은 필수입니다.");
        }
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("파일 경로는 필수입니다.");
        }
        if (fileSize <= 0) {
            throw new IllegalArgumentException("파일 크기는 0보다 커야 합니다.");
        }
        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("MIME 타입은 필수입니다.");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("허용되지 않는 MIME 타입입니다: " + contentType);
        }
    }
}