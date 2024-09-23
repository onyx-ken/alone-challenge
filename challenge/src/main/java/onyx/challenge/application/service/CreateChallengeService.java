package onyx.challenge.application.service;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.inbound.CreateChallengeUseCase;
import onyx.challenge.application.port.outbound.CreateChallengeOutputPort;
import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateChallengeService implements CreateChallengeUseCase {

    private final CreateChallengeOutputPort createChallengeOutputPort;

    @Override
    public ChallengeOutputDTO createChallenge(ChallengeInputDTO challengeInputDTO) {
        Challenge challenge = Challenge.create(null, challengeInputDTO.getUserId(), challengeInputDTO.getNickName(),
                new Period(challengeInputDTO.getStartDate(), challengeInputDTO.getEndDate()),
                GoalContent.create(challengeInputDTO.getMainContent(), challengeInputDTO.getAdditionalContent(), GoalType.valueOf(challengeInputDTO.getGoalType())),
                challengeInputDTO.getAttachedImagePaths(), challengeInputDTO.getChallengeCertificateImagePath());
        return ChallengeOutputDTO.from(createChallengeOutputPort.save(challenge));
    }

}
