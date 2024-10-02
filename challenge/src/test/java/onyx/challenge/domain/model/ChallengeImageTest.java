package onyx.challenge.domain.model;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChallengeImageTest {
    @Test
    @DisplayName("유효한 데이터로 ChallengeImage를 생성하면 성공한다")
    void whenCreatingChallengeImageWithValidData_thenSuccess() {
        // Given
        Long id = 1L;
        String originalFilename = "image.jpg";
        String storedFilename = "uuid-image.jpg";
        String filePath = "/uploads/uuid-image.jpg";
        long fileSize = 1024L;
        String contentType = "image/jpeg";

        // When
        ChallengeImage challengeImage = ChallengeImage.create(id, originalFilename, storedFilename, filePath, fileSize, contentType);

        // Then
        Assertions.assertThat(challengeImage.getId()).isEqualTo(id);
        Assertions.assertThat(challengeImage.getOriginalFilename()).isEqualTo(originalFilename);
        Assertions.assertThat(challengeImage.getStoredFilename()).isEqualTo(storedFilename);
        Assertions.assertThat(challengeImage.getFilePath()).isEqualTo(filePath);
        Assertions.assertThat(challengeImage.getFileSize()).isEqualTo(fileSize);
        Assertions.assertThat(challengeImage.getContentType()).isEqualTo(contentType);
    }

    @Test
    @DisplayName("필수 필드가 null이거나 유효하지 않으면 IllegalArgumentException이 발생한다")
    void whenRequiredFieldsAreInvalid_thenThrowsException() {
        // Given
        Long id = 1L;
        String originalFilename = null; // 필수 필드를 null로 설정
        String storedFilename = "uuid-image.jpg";
        String filePath = "/uploads/uuid-image.jpg";
        long fileSize = -1L; // 유효하지 않은 파일 크기
        String contentType = "invalid/type"; // 허용되지 않는 MIME 타입

        // When & Then
        // originalFilename이 null일 때
        Assertions.assertThatThrownBy(() -> ChallengeImage.create(id, originalFilename, storedFilename, filePath, 1024L, "image/jpeg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("원본 파일명은 필수입니다.");

        // fileSize가 0 이하일 때
        Assertions.assertThatThrownBy(() -> ChallengeImage.create(id, "image.jpg", storedFilename, filePath, fileSize, "image/jpeg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("파일 크기는 0보다 커야 합니다.");

        // 허용되지 않는 MIME 타입일 때
        Assertions.assertThatThrownBy(() -> ChallengeImage.create(id, "image.jpg", storedFilename, filePath, 1024L, contentType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("허용되지 않는 MIME 타입입니다");
    }

    @Test
    @DisplayName("원본 파일명이 너무 길면 IllegalArgumentException이 발생한다")
    void whenOriginalFilenameIsTooLong_thenThrowsException() {
        // Given
        Long id = 1L;
        String originalFilename = "a".repeat(256) + ".jpg"; // 파일명 길이가 256자
        String storedFilename = "uuid-image.jpg";
        String filePath = "/uploads/uuid-image.jpg";
        long fileSize = 1024L;
        String contentType = "image/jpeg";

        // When & Then
        Assertions.assertThatThrownBy(() -> ChallengeImage.create(id, originalFilename, storedFilename, filePath, fileSize, contentType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("원본 파일명의 길이는 255자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("유효한 MIME 타입의 파일만 허용된다")
    void whenContentTypeIsValid_thenSuccess() {
        // Given
        Long id = 1L;
        String originalFilename = "image.png";
        String storedFilename = "uuid-image.png";
        String filePath = "/uploads/uuid-image.png";
        long fileSize = 2048L;
        String contentType = "image/png";

        // When
        ChallengeImage challengeImage = ChallengeImage.create(id, originalFilename, storedFilename, filePath, fileSize, contentType);

        // Then
        Assertions.assertThat(challengeImage.getContentType()).isEqualTo(contentType);
    }

    @Test
    @DisplayName("허용되지 않는 MIME 타입의 파일이면 IllegalArgumentException이 발생한다")
    void whenContentTypeIsInvalid_thenThrowsException() {
        // Given
        Long id = 1L;
        String originalFilename = "document.pdf";
        String storedFilename = "uuid-document.pdf";
        String filePath = "/uploads/uuid-document.pdf";
        long fileSize = 4096L;
        String contentType = "application/pdf";

        // When & Then
        Assertions.assertThatThrownBy(() -> ChallengeImage.create(id, originalFilename, storedFilename, filePath, fileSize, contentType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("허용되지 않는 MIME 타입입니다");
    }
}
