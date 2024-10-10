package onyx.challenge.framework.adapter.outbound.jpa.comment;

import onyx.challenge.framework.adapter.outbound.jpa.entity.CommentJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentJPAEntity, Long> {
    List<CommentJPAEntity> getAllByChallengeId(Long id);
}
