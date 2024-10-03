package onyx.challenge.framework.adapter.outbound.jpa;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.framework.adapter.outbound.jpa.entity.ChallengeJPAEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChallengeJpaAdapter implements ChallengeRepository {

    private final ChallengeJpaRepository challengeJPARepository;

    @Override
    public Challenge save(Challenge challenge) {
        return challengeJPARepository.save(ChallengeJPAEntity.fromDomain(challenge)).toDomain();
    }

    @Override
    public Challenge load(Long id) {
        return challengeJPARepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("챌린지가 존재하지 않습니다.")).toDomain();
    }
}
