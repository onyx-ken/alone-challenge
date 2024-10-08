package onyx.challenge.framework.adapter.outbound.jpa.like;

import onyx.challenge.framework.adapter.outbound.jpa.entity.LikeJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeJpaRepository extends JpaRepository<LikeJPAEntity, Long> {
    List<LikeJPAEntity> findByChallengeId(Long challengeId);
    Optional<LikeJPAEntity> findByChallengeIdAndUserId(Long challengeId, Long userId);
    int countByChallengeId(Long challengeId);
}
