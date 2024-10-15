package onyx.challenge.framework.adapter.outbound.jpa.entity;

import onyx.challenge.domain.model.ChallengeImage;
import onyx.challenge.domain.vo.ImageType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChallengeImageJPAEntityTest {

    @Test
    @DisplayName("도메인 모델에서 JPA 엔티티로 변환한 후 다시 도메인 모델로 변환하면 동일한 데이터를 유지한다")
    void givenDomainModel_whenConvertToEntityAndBack_thenDataRemainsSame() {
        // Given
        Long id = 1L;
        String originalFilename = "image.jpg";
        String storedFilename = "uuid-image.jpg";
        String filePath = "/uploads/uuid-image.jpg";
        long fileSize = 1024L;
        String contentType = "image/jpeg";
        int imageOrder = 1;
        ImageType type = ImageType.USER_UPLOAD;

        ChallengeImage originalChallenge = ChallengeImage.from(id, originalFilename,
                storedFilename, filePath, fileSize, contentType, imageOrder, type);

        // When

        ChallengeImageJPAEntity challengeImageEntity = ChallengeImageJPAEntity.fromDomain(originalChallenge);
        ChallengeImage convertedChallengeImage = challengeImageEntity.toDomain();

        // Then
        Assertions.assertThat(convertedChallengeImage.getId()).isEqualTo(originalChallenge.getId());
        Assertions.assertThat(convertedChallengeImage.getOriginalFilename()).isEqualTo(originalChallenge.getOriginalFilename());
        Assertions.assertThat(convertedChallengeImage.getStoredFilename()).isEqualTo(originalChallenge.getStoredFilename());
        Assertions.assertThat(convertedChallengeImage.getFilePath()).isEqualTo(originalChallenge.getFilePath());
        Assertions.assertThat(convertedChallengeImage.getFileSize()).isEqualTo(originalChallenge.getFileSize());
        Assertions.assertThat(convertedChallengeImage.getContentType()).isEqualTo(originalChallenge.getContentType());
    }

    @Test
    @DisplayName("JPA 엔티티에서 도메인 모델로 변환할 때 모든 필드가 정확하게 매핑된다")
    void givenEntity_whenConvertToDomain_thenAllFieldsAreMappedCorrectly() {

        // Given
        Long id = 1L;
        String originalFilename = "image.jpg";
        String storedFilename = "uuid-image.jpg";
        String filePath = "/uploads/uuid-image.jpg";
        long fileSize = 1024L;
        String contentType = "image/jpeg";
        int imageOrder = 1;
        ImageType type = ImageType.USER_UPLOAD;

        ChallengeImageJPAEntity challengeImageEntity =
                ChallengeImageJPAEntity.testInstance(id, originalFilename, storedFilename, filePath, fileSize, contentType, imageOrder, type);

        // When
        ChallengeImage challengeImage = challengeImageEntity.toDomain();

        // Then
        Assertions.assertThat(challengeImage.getId()).isEqualTo(challengeImageEntity.getId());
        Assertions.assertThat(challengeImage.getOriginalFilename()).isEqualTo(challengeImageEntity.getOriginalFilename());
        Assertions.assertThat(challengeImage.getStoredFilename()).isEqualTo(challengeImageEntity.getStoredFilename());
        Assertions.assertThat(challengeImage.getFilePath()).isEqualTo(challengeImageEntity.getFilePath());
        Assertions.assertThat(challengeImage.getFileSize()).isEqualTo(challengeImageEntity.getFileSize());
        Assertions.assertThat(challengeImage.getContentType()).isEqualTo(challengeImageEntity.getContentType());
    }

}