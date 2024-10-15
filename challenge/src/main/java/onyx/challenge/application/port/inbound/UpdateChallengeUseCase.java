package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.ChallengeModifyDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;

public interface UpdateChallengeUseCase {
    ChallengeOutputDTO updateChallenge(ChallengeModifyDTO dto);
}
