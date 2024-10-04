package onyx.challenge.application.dto.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentInputDTO {
    private Long challengeId;
    private Long userId;
    private String content;
    private Long parentCommentId; // 선택 사항
    private Long replyToUserId;   // 선택 사항
}
