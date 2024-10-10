package onyx.challenge.framework.adapter.outbound.jpa;

import onyx.challenge.application.service.exceptiron.ChallengeImageNotFoundException;
import onyx.challenge.domain.model.ChallengeImage;
import onyx.challenge.framework.adapter.outbound.jpa.entity.ChallengeImageJPAEntity;
import onyx.challenge.framework.adapter.outbound.jpa.image.ChallengeImageJpaAdapter;
import onyx.challenge.framework.adapter.outbound.jpa.image.ChallengeImageJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChallengeImageJpaAdapterTest {

    @Mock
    private ChallengeImageJpaRepository challengeImageJpaRepository;

    @InjectMocks
    private ChallengeImageJpaAdapter challengeImageJpaAdapter;

    @Test
    @DisplayName("ChallengeImage를 저장하고 조회할 수 있다")
    void whenSaveAndFindChallengeImage_thenSuccess() {
        // Given
        ChallengeImage image = ChallengeImage.create(
                1L,
                "original-filename.jpg",
                "stored-filename.jpg",
                "/path/to/stored-filename.jpg",
                1024L,
                "image/jpeg"
        );

        ChallengeImageJPAEntity entity = ChallengeImageJPAEntity.fromDomain(image);

        when(challengeImageJpaRepository.save(any(ChallengeImageJPAEntity.class))).thenReturn(entity);
        when(challengeImageJpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        // When
        ChallengeImage savedImage = challengeImageJpaAdapter.saveImage(image);
        ChallengeImage foundImage = challengeImageJpaAdapter.loadImage(1L)
                .orElseThrow(() -> new ChallengeImageNotFoundException(image.getId()));

        // Then
        assertThat(savedImage).isNotNull();
        assertThat(savedImage.getId()).isEqualTo(1L);

        assertThat(foundImage).isNotNull();
        assertThat(foundImage.getId()).isEqualTo(1L);
        assertThat(foundImage.getOriginalFilename()).isEqualTo("original-filename.jpg");
    }

    @Test
    @DisplayName("존재하지 않는 ChallengeImage를 조회하려고 하면 예외가 발생한다")
    void whenFindNonExistentChallengeImage_thenThrowsException() {
        // Given
        when(challengeImageJpaRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> challengeImageJpaAdapter.loadImage(1L))
                .isInstanceOf(ChallengeImageNotFoundException.class);
    }
}
