package onyx.challenge.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import onyx.challenge.domain.model.Comment;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentOutputDTO {
    private Long commentId;
    private Long challengeId;
    private Long userId;
    private String content;
    private Long parentCommentId; // 선택 사항
    private Long replyToUserId;   // 선택 사항
    private LocalDateTime createdAt;

    public static CommentOutputDTO from(Comment comment) {
        return new CommentOutputDTO(
                comment.getCommentId(),
                comment.getChallengeId(),
                comment.getUserId(),
                comment.getContent(),
                comment.getParentCommentId(),
                comment.getReplyToUserId(),
                comment.getCreatedAt()
        );
    }
}