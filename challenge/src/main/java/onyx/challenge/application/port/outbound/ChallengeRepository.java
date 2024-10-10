package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.Challenge;

import java.util.Optional;

public interface ChallengeRepository {
    Challenge save(Challenge challenge);
    Optional<Challenge> load(Long id);
}
