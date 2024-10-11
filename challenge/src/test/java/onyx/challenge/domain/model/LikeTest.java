package onyx.challenge.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(like.getLikeId()).isEqualTo(likeId);
        assertThat(like.getChallengeId()).isEqualTo(challengeId);
        assertThat(like.getUserId()).isEqualTo(userId);
        assertThat(like.getLikedAt()).isEqualTo(likedAt);
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

    @Test
    @DisplayName("likedAt이 null이면 현재 시간으로 설정된다")
    void whenLikedAtIsNull_thenSetToCurrentTime() {
        // Given
        Long likeId = 1L;
        Long challengeId = 100L;
        Long userId = 200L;
        LocalDateTime beforeCreation = LocalDateTime.now();

        // When
        Like like = Like.create(likeId, challengeId, userId, null);

        // Then
        assertThat(like.getLikedAt()).isAfterOrEqualTo(beforeCreation);
    }


}