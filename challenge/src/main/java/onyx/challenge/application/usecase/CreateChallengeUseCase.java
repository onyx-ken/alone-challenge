package onyx.challenge.application.usecase;

import onyx.challenge.application.usecase.dto.ChallengeInputDTO;
import onyx.challenge.application.usecase.dto.ChallengeOutputDTO;

public interface CreateChallengeUseCase {
    ChallengeOutputDTO createChallenge(ChallengeInputDTO challengeInputDTO);
}
