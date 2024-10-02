package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.Comment;

public interface CommentRepository {
    Comment save(Comment comment);
}
