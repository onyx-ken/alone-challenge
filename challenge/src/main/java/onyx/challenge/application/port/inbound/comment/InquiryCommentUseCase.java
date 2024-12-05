package onyx.challenge.application.port.inbound.comment;

import onyx.challenge.application.dto.comment.CommentOutputDTO;

import java.util.List;

public interface InquiryCommentUseCase {
    List<CommentOutputDTO> getCommentsByChallengeId(Long challengeId);
}
