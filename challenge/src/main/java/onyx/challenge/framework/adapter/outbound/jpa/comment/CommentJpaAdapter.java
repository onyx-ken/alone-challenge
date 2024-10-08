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
    public Comment load(Long commentId) {
        return commentJpaRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found")).toDomain();
    }

    @Override
    public Comment update(Comment comment) {
        CommentJPAEntity commentJPAEntity = commentJpaRepository.findById(comment.getCommentId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentJPAEntity.setContent(comment.getContent());
        commentJPAEntity.setUpdatedAt(LocalDateTime.now());

        return commentJPAEntity.toDomain();
    }

    @Override
    public void deleteById(Long commentId) {

    }

    @Override
    public List<Comment> getListByChallengeId(Long challengeId) {
        return List.of();
    }
}
