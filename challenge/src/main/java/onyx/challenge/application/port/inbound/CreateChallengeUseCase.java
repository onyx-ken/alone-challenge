package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;

public interface CreateChallengeUseCase {
    ChallengeOutputDTO createChallenge(ChallengeInputDTO challengeInputDTO);
}
