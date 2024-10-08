package onyx.challenge.application.port.inbound.comment;

import onyx.challenge.application.dto.comment.CommentModifyDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;

public interface UpdateCommentUseCase {
    CommentOutputDTO updateComment(CommentModifyDTO dto);
}
