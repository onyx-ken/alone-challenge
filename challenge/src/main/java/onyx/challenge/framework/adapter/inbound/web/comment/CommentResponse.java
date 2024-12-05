package onyx.challenge.framework.adapter.inbound.web.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import onyx.challenge.application.dto.comment.CommentOutputDTO;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class CommentResponse {
    private final String commentId;
    private final String challengeId;
    private final String userId;
    private final String content;
    private final String parentCommentId;
    private final String replyToUserId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;

    public static CommentResponse create(CommentOutputDTO dto) {
        return new CommentResponse(
                Objects.toString(dto.getCommentId(), null),
                Objects.toString(dto.getChallengeId(), null),
                Objects.toString(dto.getUserId(), null),
                dto.getContent(),
                Objects.toString(dto.getParentCommentId(), null),
                Objects.toString(dto.getReplyToUserId(), null),
                dto.getCreatedAt());
    }
}
