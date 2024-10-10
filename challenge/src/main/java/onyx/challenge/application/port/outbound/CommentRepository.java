package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> load(Long commentId);
    List<Comment> getListByChallengeId(Long challengeId);
}
