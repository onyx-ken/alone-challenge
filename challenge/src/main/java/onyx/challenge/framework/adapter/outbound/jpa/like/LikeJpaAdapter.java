package onyx.challenge.framework.adapter.outbound.jpa.like;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.application.service.LikeNotFoundException;
import onyx.challenge.domain.model.Like;
import onyx.challenge.framework.adapter.outbound.jpa.entity.LikeJPAEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeJpaAdapter implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Like save(Like like) {
        return likeJpaRepository.save(LikeJPAEntity.fromDomain(like)).toDomain();
    }

    @Override
    public Like load(Long likeId) {
        return likeJpaRepository.findById(likeId).orElseThrow(() ->
                new LikeNotFoundException("좋아요가 존재하지 않습니다.")).toDomain();
    }

    @Override
    public int countByChallengeId(Long challengeId) {
        return likeJpaRepository.countByChallengeId(challengeId);
    }

    @Override
    public void deleteById(Long likeId) {
        likeJpaRepository.deleteById(likeId);
    }

    @Override
    public Optional<Like> findByChallengeIdAndUserId(Long challengeId, Long userId) {
        return likeJpaRepository.findByChallengeIdAndUserId(challengeId, userId)
                .map(LikeJPAEntity::toDomain);
    }
}
