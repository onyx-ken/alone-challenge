package onyx.challenge.framework.adapter.outbound.jpa;

import onyx.challenge.framework.adapter.outbound.jpa.entity.LikeJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeJpaRepository extends JpaRepository<LikeJPAEntity, Long> {
    List<LikeJPAEntity> findByChallengeId(Long challengeId);
}
