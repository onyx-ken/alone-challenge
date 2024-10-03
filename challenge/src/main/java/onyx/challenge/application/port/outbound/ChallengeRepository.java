package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.Challenge;

public interface ChallengeRepository {
    Challenge save(Challenge challenge);
    Challenge load(Long id);
}
