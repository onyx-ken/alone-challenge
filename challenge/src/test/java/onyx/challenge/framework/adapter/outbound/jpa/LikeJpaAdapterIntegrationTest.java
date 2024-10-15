package onyx.challenge.framework.adapter.outbound.jpa;

import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.application.service.exceptiron.like.AlreadyLikedException;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.model.Like;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.Period;
import onyx.challenge.framework.adapter.outbound.jpa.entity.LikeJPAEntity;
import onyx.challenge.framework.adapter.outbound.jpa.like.LikeJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

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

    @BeforeEach
    void setUp(@Autowired ChallengeRepository challengeRepository) {
        Challenge challenge = Challenge.create(
                null,
                1L,
                "JohnDoe",
                new Period(LocalDate.of(2024,9,19), LocalDate.of(2024, 9 ,21)),
                GoalContent.create("Run 5K", "Do It!", GoalType.POSITIVE),
                Arrays.asList(5L, 6L)
        );
        challengeRepository.save(challenge);
    }

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
    @DisplayName("중복된 좋아요를 저장하려고 하면 AlreadyLikedException 이 발생한다")
    public void givenDuplicateLike_whenSave_thenAlreadyLikedExceptionIsThrown() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        Like like = Like.create(null, challengeId, userId, null);
        Like like2 = Like.create(null, challengeId, userId, null);

        // When
        likeRepository.save(like);

        // Then
        assertThatThrownBy(() -> likeRepository.save(like2))
                .isInstanceOf(AlreadyLikedException.class)
                .hasMessageContaining("like.already.exists");
    }
}
