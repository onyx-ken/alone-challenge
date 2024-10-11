package onyx.challenge.application.service;

import onyx.challenge.application.dto.like.LikeInputDTO;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.application.service.exceptiron.like.LikeNotFoundException;
import onyx.challenge.application.service.like.DeleteLikeService;
import onyx.challenge.domain.model.Like;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteLikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private DeleteLikeService deleteLikeService;

    @Test
    @DisplayName("유효한 입력으로 좋아요 취소 시 성공한다")
    void givenValidLikeInputDTO_whenDeleteLike_thenSuccess() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        Long likeId = 10L;

        Like existingLike = Like.create(
                likeId,
                challengeId,
                userId,
                LocalDateTime.now()
        );

        LikeInputDTO likeInputDTO = LikeInputDTO.builder()
                .challengeId(challengeId)
                .userId(userId)
                .build();

        given(likeRepository.findByChallengeIdAndUserId(challengeId, userId))
                .willReturn(Optional.of(existingLike));

        // When
        deleteLikeService.deleteLike(likeInputDTO);

        // Then
        verify(likeRepository, times(1)).findByChallengeIdAndUserId(challengeId, userId);
        verify(likeRepository, times(1)).deleteById(likeId);
    }

    @Test
    @DisplayName("좋아요가 존재하지 않을 때 LikeNotFoundException이 발생한다")
    void givenNonExistingLike_whenDeleteLike_thenThrowsLikeNotFoundException() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;

        LikeInputDTO likeInputDTO = LikeInputDTO.builder()
                .challengeId(challengeId)
                .userId(userId)
                .build();

        given(likeRepository.findByChallengeIdAndUserId(challengeId, userId))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> deleteLikeService.deleteLike(likeInputDTO))
                .isInstanceOf(LikeNotFoundException.class);

        verify(likeRepository, times(1)).findByChallengeIdAndUserId(challengeId, userId);
        verify(likeRepository, never()).deleteById(anyLong());
    }
}