package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.CommentInputDTO;
import onyx.challenge.application.dto.CommentOutputDTO;

public interface CreateCommentUseCase {
    CommentOutputDTO createComment(CommentInputDTO commentInputDTO);
}
