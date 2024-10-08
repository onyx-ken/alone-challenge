package onyx.challenge.application.service;

import onyx.challenge.application.dto.like.LikeInputDTO;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.domain.model.Like;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteLikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private DeleteLikeService deleteLikeService;

    @Test
    void givenExistingLike_whenDeleteLike_thenLikeIsDeleted() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        Like like = Like.create(1L, challengeId, userId, LocalDateTime.now());

        when(likeRepository.findByChallengeIdAndUserId(challengeId, userId))
                .thenReturn(Optional.of(like));
        // When
        deleteLikeService.deleteLike(new LikeInputDTO(challengeId, userId));

        // Then
        verify(likeRepository).deleteById(like.getLikeId());


    }

    @Test
    void givenNonExistingLike_whenDeleteLike_thenThrowsException() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;

        when(likeRepository.findByChallengeIdAndUserId(challengeId, userId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> deleteLikeService.deleteLike(new LikeInputDTO(challengeId, userId)))
                .isInstanceOf(LikeNotFoundException.class)
                .hasMessageContaining("좋아요가 존재하지 않습니다.");
    }
}