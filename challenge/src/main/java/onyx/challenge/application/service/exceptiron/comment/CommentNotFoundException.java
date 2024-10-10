package onyx.challenge.application.service.exceptiron.comment;

import onyx.challenge.application.service.exceptiron.common.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(Long commentId) {
        super("comment.not.found", commentId);
    }
}
