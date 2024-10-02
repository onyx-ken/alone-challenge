package onyx.challenge.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Comment {

    private final Long commentId;
    private final Long challengeId;
    private final Long userId;
    private final String content;
    private final Long parentCommentId; // 최상위 댓글인 경우 null
    private final Long replyToUserId; // 대댓글의 경우 답글 대상 사용자 ID
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static Comment create(Long commentId, Long challengeId, Long userId, String content,
                                 Long parentCommentId, Long replyToUserId,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {

        validateRequiredFields(challengeId, userId, content);

        if (parentCommentId != null && replyToUserId != null) {
            // 대댓글에 대한 로직 처리 (답글 대상이 존재)
        } else if (parentCommentId != null) {
            // 대댓글 (답글 대상 없음)
        } else {
            // 최상위 댓글
        }

        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }

        return new Comment(commentId, challengeId, userId, content,
                parentCommentId, replyToUserId, createdAt, updatedAt);
    }

    private Comment(Long commentId, Long challengeId, Long userId, String content,
                    Long parentCommentId, Long replyToUserId,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.commentId = commentId;
        this.challengeId = challengeId;
        this.userId = userId;
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.replyToUserId = replyToUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private static void validateRequiredFields(Long challengeId, Long userId, String content) {
        if (challengeId == null) {
            throw new IllegalArgumentException("챌린지 ID는 필수입니다.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 필수입니다.");
        }
    }

}
