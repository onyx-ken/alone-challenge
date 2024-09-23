package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.Challenge;

public interface CreateChallengeOutputPort {
    Challenge save(Challenge challenge);
}
