package onyx.challenge.framework.adapter.inbound.web.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreateRequest {
    @NotBlank(message = "comment.content.required")
    private String content;
    private Long parentCommentId;
    private Long replyToUserId;
}
