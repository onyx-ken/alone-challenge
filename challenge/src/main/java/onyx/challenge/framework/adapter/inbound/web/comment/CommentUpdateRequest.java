package onyx.challenge.framework.adapter.inbound.web.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentUpdateRequest {
    @NotBlank(message = "comment.content.required")
    private String content;
}
