package onyx.challenge.application.port.inbound.comment;

import onyx.challenge.application.dto.comment.CommentInputDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;

public interface CreateCommentUseCase {
    CommentOutputDTO createComment(CommentInputDTO commentInputDTO);
}
