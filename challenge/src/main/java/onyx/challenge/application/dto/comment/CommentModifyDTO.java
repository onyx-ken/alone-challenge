package onyx.challenge.application.dto.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentModifyDTO {
    private Long commentId;
    private Long userId;
    private String content;
}
