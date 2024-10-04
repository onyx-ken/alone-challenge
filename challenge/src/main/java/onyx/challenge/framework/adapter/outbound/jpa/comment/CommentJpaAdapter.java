package onyx.challenge.framework.adapter.outbound.jpa.comment;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.domain.model.Comment;
import onyx.challenge.framework.adapter.outbound.jpa.entity.CommentJPAEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentJpaAdapter implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        CommentJPAEntity entity = CommentJPAEntity.fromDomain(comment);
        CommentJPAEntity savedEntity = commentJpaRepository.save(entity);
        return savedEntity.toDomain();
    }
}
