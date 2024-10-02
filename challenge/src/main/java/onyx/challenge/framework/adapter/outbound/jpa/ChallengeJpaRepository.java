package onyx.challenge.framework.adapter.outbound.jpa;

import onyx.challenge.framework.adapter.outbound.jpa.entity.ChallengeJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeJpaRepository extends JpaRepository<ChallengeJPAEntity, Long> {
    List<ChallengeJPAEntity> findAllByActiveIsTrueOrderByPeriodStartDateDesc();
}
