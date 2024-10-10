package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.ChallengeInquiryOutputDTO;
import onyx.challenge.application.dto.ChallengeViewDTO;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.application.service.InquiryChallengeService;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InquiryChallengeUseCaseTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private InquiryChallengeService inquiryChallengeService;

    @Test
    @DisplayName("유효한 challengeId로 조회하면 ChallengeInquiryOutputDTO를 반환한다")
    void givenValidChallengeId_whenInquiryChallenge_thenReturnsChallengeInquiryOutputDTO() {
        // Given
        Long challengeId = 1L;
        Challenge challenge = Challenge.create(
                challengeId,
                1L,
                "JohnDoe",
                new Period(LocalDate.of(2024, 9, 19), LocalDate.of(2024, 9, 21)),
                GoalContent.create("Run 5K", "Do It!", GoalType.POSITIVE),
                Arrays.asList(5L, 6L)
        );
        int likeCount = 10;

        given(challengeRepository.load(challengeId)).willReturn(Optional.of(challenge));
        given(likeRepository.countByChallengeId(challengeId)).willReturn(likeCount);

        // When
        ChallengeViewDTO result = inquiryChallengeService.getChallengeDetail(challengeId);

        // Then
        assertThat(result).isNotNull();
        // assertThat(result.getChallengeId()).isEqualTo(challengeId);
        // assertThat(result.getUserId()).isEqualTo(challenge.getUserId());
        // assertThat(result.getLikeCount()).isEqualTo(likeCount);
    }
}