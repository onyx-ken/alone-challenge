package onyx.challenge.framework.adapter.outbound.jpa.image;

import onyx.challenge.framework.adapter.outbound.jpa.entity.ChallengeImageJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeImageJpaRepository extends JpaRepository<ChallengeImageJPAEntity, Long> {
}
