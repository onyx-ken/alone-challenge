package onyx.challenge.framework.adapter.out.jpa;

import onyx.challenge.framework.adapter.out.jpa.entity.ChallengeJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<ChallengeJPAEntity, Long> {
}
