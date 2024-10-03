package onyx.challenge.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class LikeTest {

    @Test
    @DisplayName("유효한 데이터로 Like를 생성하면 성공한다")
    void whenCreatingLikeWithValidData_thenSuccess() {
        // Given
        Long likeId = 1L;
        Long challengeId = 100L;
        Long userId = 200L;
        LocalDateTime likedAt = LocalDateTime.of(2024, 9, 10, 14, 20);

        // When
        Like like = Like.create(likeId, challengeId, userId, likedAt);

        // Then

        Assertions.assertThat(like.getLikeId()).isEqualTo(likeId);
        Assertions.assertThat(like.getChallengeId()).isEqualTo(challengeId);
        Assertions.assertThat(like.getUserId()).isEqualTo(userId);
        Assertions.assertThat(like.getLikedAt()).isEqualTo(likedAt);
    }

    @Test
    @DisplayName("필수 필드가 null이면 IllegalArgumentException이 발생한다")
    void whenRequiredFieldsAreNull_thenThrowsException() {
        // Given
        Long likeId = 1L;
        Long challengeId = null;
        Long userId = null;
        LocalDateTime likedAt = LocalDateTime.of(2024, 9, 10, 14, 20);

        // When & Then
        Assertions.assertThatThrownBy(() -> Like.create(likeId, challengeId, userId, likedAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("challengeId와 userId는 반드시 포함되어야 합니다.");
    }

}