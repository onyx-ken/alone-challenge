package onyx.challenge.framework.adapter.outbound.jpa;

import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.domain.model.Like;
import onyx.challenge.framework.adapter.outbound.jpa.entity.LikeJPAEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LikeJpaAdapterIntegrationTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Test
    @DisplayName("유효한 Like를 저장하면 데이터베이스에 저장된다")
    public void givenLike_whenSave_thenLikeIsPersistedInDatabase() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        Like like = Like.create(null, challengeId, userId, null);

        // When
        Like savedLike = likeRepository.save(like);

        // Then
        assertThat(savedLike).isNotNull();
        assertThat(savedLike.getLikeId()).isNotNull();
        assertThat(savedLike.getChallengeId()).isEqualTo(challengeId);
        assertThat(savedLike.getUserId()).isEqualTo(userId);

        // 데이터베이스에 저장되었는지 검증
        LikeJPAEntity foundEntity = likeJpaRepository.findById(savedLike.getLikeId()).orElse(null);
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getChallengeId()).isEqualTo(challengeId);
        assertThat(foundEntity.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("중복된 좋아요를 저장하려고 하면 DataIntegrityViolationException이 발생한다")
    public void givenDuplicateLike_whenSave_thenDataIntegrityViolationExceptionIsThrown() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        Like like1 = Like.create(null, challengeId, userId, null);
        Like like2 = Like.create(null, challengeId, userId, null);

        // When
        likeRepository.save(like1);

        // Then
        assertThatThrownBy(() -> likeRepository.save(like2))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Unique index or primary key violation");
    }

}
