package onyx.challenge.framework.adapter.outbound.jpa.entity;

import onyx.challenge.domain.model.Like;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LikeJPAEntityTest {

    @Test
    @DisplayName("도메인 모델에서 JPA 엔티티로 변환한 후 다시 도메인 모델로 변환하면 동일한 데이터를 유지한다")
    void givenDomainModel_whenConvertToEntityAndBack_thenDataRemainsSame() {
        // Given
        Long likeId = 1L;
        Long challengeId = 100L;
        Long userId = 200L;
        LocalDateTime likedAt = LocalDateTime.of(2024, 9, 10, 14, 20);
        Like originalLike = Like.create(likeId, challengeId, userId, likedAt);

        // When

        LikeJPAEntity likeJPAEntity = LikeJPAEntity.fromDomain(originalLike);
        Like convertedLike = likeJPAEntity.toDomain();

        // Then
        assertThat(convertedLike).isNotNull();
        assertThat(convertedLike.getLikeId()).isEqualTo(likeJPAEntity.getLikeId());
        assertThat(convertedLike.getChallengeId()).isEqualTo(likeJPAEntity.getChallengeId());
        assertThat(convertedLike.getUserId()).isEqualTo(likeJPAEntity.getUserId());
        assertThat(convertedLike.getLikedAt()).isEqualTo(likeJPAEntity.getLikedAt());
    }

    @Test
    @DisplayName("JPA 엔티티에서 도메인 모델로 변환할 때 모든 필드가 정확하게 매핑된다")
    void givenEntity_whenConvertToDomain_thenAllFieldsAreMappedCorrectly() {
        // Given
        LikeJPAEntity likeJPAEntity = LikeJPAEntity.testInstance(1L, 100L, 200L,
                LocalDateTime.of(2024, 9, 10, 14, 20));

        // When
        Like like = likeJPAEntity.toDomain();

        // Then
        assertThat(like).isNotNull();
        assertThat(like.getLikeId()).isEqualTo(likeJPAEntity.getLikeId());
        assertThat(like.getChallengeId()).isEqualTo(likeJPAEntity.getChallengeId());
        assertThat(like.getUserId()).isEqualTo(likeJPAEntity.getUserId());
        assertThat(like.getLikedAt()).isEqualTo(likeJPAEntity.getLikedAt());
    }

}