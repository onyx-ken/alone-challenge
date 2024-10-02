package onyx.challenge.framework.adapter.outbound.jpa;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.domain.model.Like;
import onyx.challenge.framework.adapter.outbound.jpa.entity.LikeJPAEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeJpaAdapter implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Like save(Like like) {
        return likeJpaRepository.save(LikeJPAEntity.fromDomain(like)).toDomain();
    }

    @Override
    public List<Like> load(Long ChallengeId) {
        return likeJpaRepository.findByChallengeId(ChallengeId).stream().map(LikeJPAEntity::toDomain).toList();
    }

    @Override
    public int countByChallengeId(Long challengeId) {
        return load(challengeId).size();
    }

}
