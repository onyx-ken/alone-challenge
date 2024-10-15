package onyx.challenge.application.service;

import onyx.challenge.application.dto.FileData;
import onyx.challenge.application.port.outbound.ChallengeImageRepository;
import onyx.challenge.application.port.outbound.FileStorage;
import onyx.challenge.application.service.exceptiron.ChallengeImageNotFoundException;
import onyx.challenge.domain.model.ChallengeImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InquiryChallengeImageServiceTest {

    @Mock
    private ChallengeImageRepository challengeImageRepository;

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private InquiryChallengeImageService inquiryChallengeImageService;

    @Test
    @DisplayName("유효한 이미지 ID로 이미지 조회 시 FileData를 반환한다")
    void givenValidImageId_whenGetImage_thenReturnsFileData() {
        // Given
        Long imageId = 1L;
        ChallengeImage challengeImage = ChallengeImage.create(
                imageId,
                "original.jpg",
                "stored.jpg",
                "/path/to/stored.jpg",
                1024L,
                "image/jpeg",
                1,
                ImageType.USER_UPLOAD
        );

        byte[] imageContent = "image data".getBytes(StandardCharsets.UTF_8);

        given(challengeImageRepository.loadImage(imageId)).willReturn(Optional.of(challengeImage));
        given(fileStorage.load(challengeImage.getStoredFilename())).willReturn(imageContent);

        // When
        FileData result = inquiryChallengeImageService.getImage(imageId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOriginalFilename()).isEqualTo(challengeImage.getOriginalFilename());
        assertThat(result.getContent()).isEqualTo(imageContent);
        assertThat(result.getContentType()).isEqualTo(challengeImage.getContentType());

        verify(challengeImageRepository, times(1)).loadImage(imageId);
        verify(fileStorage, times(1)).load(challengeImage.getStoredFilename());
    }

    @Test
    @DisplayName("존재하지 않는 이미지 ID로 조회 시 ChallengeImageNotFoundException이 발생한다")
    void givenNonExistingImageId_whenGetImage_thenThrowsChallengeImageNotFoundException() {
        // Given
        Long imageId = 1L;

        given(challengeImageRepository.loadImage(imageId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> inquiryChallengeImageService.getImage(imageId))
                .isInstanceOf(ChallengeImageNotFoundException.class);

        verify(challengeImageRepository, times(1)).loadImage(imageId);
        verify(fileStorage, never()).load(anyString());
    }
}
