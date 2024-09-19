package onyx.challenge.framework.adapter.jpaadapter;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.out.CreateChallengeOutputPort;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.framework.adapter.jpaadapter.entity.ChallengeJPAEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChallengeJpaAdapter implements CreateChallengeOutputPort {

    private final ChallengeRepository challengeRepository;

    @Override
    public Challenge save(Challenge challenge) {
        return challengeRepository.save(ChallengeJPAEntity.fromDomain(challenge)).toDomain();
    }

}
