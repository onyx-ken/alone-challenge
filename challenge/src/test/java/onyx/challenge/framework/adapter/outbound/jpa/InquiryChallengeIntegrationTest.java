package onyx.challenge.framework.adapter.outbound.jpa;

import onyx.challenge.application.dto.ChallengeInquiryOutputDTO;
import onyx.challenge.application.port.inbound.InquiryChallengeUseCase;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.model.Like;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class InquiryChallengeIntegrationTest {

    @Autowired
    private InquiryChallengeUseCase inquiryChallengeUseCase;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Test
    @DisplayName("유효한 challengeId로 조회하면 ChallengeInquiryOutputDTO를 반환한다")
    void givenValidChallengeId_whenInquiryChallenge_thenReturnsChallengeInquiryOutputDTO() {
        // Given
        Long challengeId = createTestChallenge();
        createTestLikes(challengeId, 5);

        // When
        ChallengeInquiryOutputDTO result = inquiryChallengeUseCase.inquiryChallenge(challengeId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChallengeId()).isEqualTo(challengeId);
        assertThat(result.getLikeCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("존재하지 않는 challengeId로 조회하면 InvalidDataAccessApiUsageException이 발생한다")
    void givenInvalidChallengeId_whenInquiryChallenge_thenThrowsNotFoundException() {
        // Given
        Long challengeId = 999L;

        // When & Then
        assertThatThrownBy(() -> inquiryChallengeUseCase.inquiryChallenge(challengeId))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessageContaining("챌린지가 존재하지 않습니다.");
    }

    // 테스트용 챌린지 생성 메서드
    private Long createTestChallenge() {
        Challenge challenge = Challenge.create(
                null,
                1L,
                "JohnDoe",
                new Period(LocalDate.of(2024, 9, 19), LocalDate.of(2024, 9, 21)),
                GoalContent.create("Run 5K", "Do It!", GoalType.POSITIVE),
                Arrays.asList(5L, 6L)

        );
        Challenge savedChallenge = challengeRepository.save(challenge);
        return savedChallenge.getChallengeId();
    }

    // 테스트용 좋아요 생성 메서드
    private void createTestLikes(Long challengeId, int count) {
        for (int i = 1; i <= count; i++) {
            Like like = Like.create(null, challengeId, (long) i, null);
            likeRepository.save(like);
        }
    }

}
