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
    private final boolean isDeleted;

    public static Comment create(Long commentId, Long challengeId, Long userId, String content,
                                 Long parentCommentId, Long replyToUserId,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {

        validateRequiredFields(challengeId, userId, content);

        if (parentCommentId == null && replyToUserId != null) {
            throw new IllegalArgumentException("대댓글의 대상 ID가 없습니다.");
        }

        if (replyToUserId == null && parentCommentId != null) {
            throw new IllegalArgumentException("대댓글의 대상 유저 ID가 없습니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }

        return new Comment(commentId, challengeId, userId, content,
                parentCommentId, replyToUserId, createdAt, updatedAt, false);
    }

    public Comment update(String newContent, Long challengeId, LocalDateTime updatedAt) {

        if (this.isDeleted) {
            throw new IllegalStateException("삭제된 댓글은 수정할 수 없습니다.");
        }

        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 필수입니다.");
        }

        if (!this.challengeId.equals(challengeId)) {
            throw new IllegalStateException("기존의 챌린지에 속한 댓글만 수정할 수 있습니다.");
        }

        return new Comment(
                this.commentId,
                this.challengeId,
                this.userId,
                newContent,
                this.parentCommentId,
                this.replyToUserId,
                this.createdAt,
                updatedAt,
                false
        );
    }

    public Comment delete(Long challengeId, LocalDateTime updatedAt) {

        if (this.isDeleted) {
            throw new IllegalStateException("이미 삭제된 댓글 입니다.");
        }

        if (!this.challengeId.equals(challengeId)) {
            throw new IllegalStateException("기존의 챌린지에 속한 댓글만 삭제할 수 있습니다.");
        }

        return new Comment(
                this.commentId,
                this.challengeId,
                this.userId,
                this.content,
                this.parentCommentId,
                this.replyToUserId,
                this.createdAt,
                updatedAt,
                true
        );
    }

    private Comment(Long commentId, Long challengeId, Long userId, String content,
                    Long parentCommentId, Long replyToUserId,
                    LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDeleted) {
        this.commentId = commentId;
        this.challengeId = challengeId;
        this.userId = userId;
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.replyToUserId = replyToUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
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
