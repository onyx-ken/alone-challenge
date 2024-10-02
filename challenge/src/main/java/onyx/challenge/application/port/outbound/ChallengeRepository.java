package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.Challenge;

import java.util.List;

public interface ChallengeRepository {
    Challenge save(Challenge challenge);
    Challenge load(Long id);
    List<Challenge> loadAll();
}
