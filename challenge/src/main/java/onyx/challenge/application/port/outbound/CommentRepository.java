package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.Comment;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);
    Comment load(Long commentId);
    Comment update(Comment comment);
    void deleteById(Long commentId);
    List<Comment> getListByChallengeId(Long challengeId);
}
