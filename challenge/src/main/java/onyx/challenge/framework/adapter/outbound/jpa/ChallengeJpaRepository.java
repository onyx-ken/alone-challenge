package onyx.challenge.framework.adapter.outbound.jpa;

import onyx.challenge.framework.adapter.outbound.jpa.entity.ChallengeJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeJpaRepository extends JpaRepository<ChallengeJPAEntity, Long> {
}
