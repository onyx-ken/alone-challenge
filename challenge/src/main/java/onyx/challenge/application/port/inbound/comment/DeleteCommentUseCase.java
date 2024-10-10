package onyx.challenge.application.port.inbound.comment;

import onyx.challenge.application.dto.comment.CommentModifyDTO;

public interface DeleteCommentUseCase {
    void deleteComment(CommentModifyDTO dto);
}
