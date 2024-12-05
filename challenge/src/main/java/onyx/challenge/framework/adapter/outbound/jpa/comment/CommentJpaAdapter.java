package onyx.challenge.framework.adapter.outbound.jpa.comment;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.domain.model.Comment;
import onyx.challenge.framework.adapter.outbound.jpa.entity.CommentJPAEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Comment> load(Long commentId) {
        return commentJpaRepository.findById(commentId).map(CommentJPAEntity::toDomain);
    }

    @Override
    public List<Comment> getListByChallengeId(Long challengeId) {
        return commentJpaRepository.getAllByChallengeId(challengeId).stream().map(CommentJPAEntity::toDomain).toList();
    }

    @Override
    public List<Comment> findAll() {
        return commentJpaRepository.findAll().stream().map(CommentJPAEntity::toDomain).toList();
    }
}
