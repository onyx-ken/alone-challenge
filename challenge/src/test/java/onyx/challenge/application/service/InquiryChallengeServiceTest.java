package onyx.challenge.application.service;

import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.port.outbound.LikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InquiryChallengeServiceTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private InquiryChallengeService inquiryChallengeService;
    
    // todo 추후 테스트 구현
    @Test
    @DisplayName("유효한 challengeId로 조회하면 ChallengeInquiryOutputDTO를 반환한다")
    void givenValidChallengeId_whenInquiryChallenge_thenReturnsChallengeInquiryOutputDTO() {

    }
}