package onyx.challenge.application.port;

import onyx.challenge.application.usecase.dto.ChallengeInputDTO;
import onyx.challenge.application.usecase.dto.ChallengeOutputDTO;
import onyx.challenge.application.port.in.CreateChallengeInputPort;
import onyx.challenge.application.port.out.CreateChallengeOutputPort;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CreateChallengeInputPortTest {

    @Mock
    private CreateChallengeOutputPort createChallengeOutputPort;

    @InjectMocks
    private CreateChallengeInputPort createChallengeInputPort;

    private ChallengeInputDTO challengeInputDTO;

    @BeforeEach
    public void setUp() {
        challengeInputDTO = ChallengeInputDTO.builder()
                .userId(1L)
                .nickName("JohnDoe")
                .startDate(LocalDate.of(2024, 9, 19))
                .endDate(LocalDate.of(2024, 9, 25))
                .mainContent("Run 5K")
                .additionalContent("Do It!")
                .goalType("POSITIVE")
                .attachedImagePaths(List.of("path/to/image1.jpg", "path/to/image2.jpg"))
                .challengeCertificateImagePath("path/to/certificate.jpg")
                .build();
    }

    @Test
    public void givenValidChallengeInputDTO_whenCreateChallenge_thenReturnsChallengeOutputDTO() {
        // Given
        Challenge challenge = Challenge.create(
                null,
                challengeInputDTO.getUserId(),
                challengeInputDTO.getNickName(),
                new Period(challengeInputDTO.getStartDate(), challengeInputDTO.getEndDate()),
                GoalContent.create(challengeInputDTO.getMainContent(), challengeInputDTO.getAdditionalContent(), GoalType.valueOf(challengeInputDTO.getGoalType())),
                challengeInputDTO.getAttachedImagePaths(),
                challengeInputDTO.getChallengeCertificateImagePath()
        );

        given(createChallengeOutputPort.save(any(Challenge.class))).willReturn(challenge);

        // When
        ChallengeOutputDTO result = createChallengeInputPort.createChallenge(challengeInputDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChallengeId()).isNull();

    }

}