package onyx.challenge.application.port.out;

import onyx.challenge.domain.model.Challenge;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateChallengeOutputPort {
    Challenge save(Challenge challenge);
}
