package onyx.challenge.framework.adapter.outbound.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.challenge.domain.model.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private Long challengeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 1000)
    private String content;

    private Long parentCommentId; // 최상위 댓글인 경우 null

    private Long replyToUserId; // 답글 대상 사용자 ID

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 도메인 모델로부터 변환
    public static CommentJPAEntity fromDomain(Comment comment) {
        CommentJPAEntity entity = new CommentJPAEntity();
        entity.commentId = comment.getCommentId();
        entity.challengeId = comment.getChallengeId();
        entity.userId = comment.getUserId();
        entity.content = comment.getContent();
        entity.parentCommentId = comment.getParentCommentId();
        entity.replyToUserId = comment.getReplyToUserId();
        entity.createdAt = comment.getCreatedAt();
        entity.updatedAt = comment.getUpdatedAt();
        return entity;
    }

    // 도메인 모델로 변환
    public Comment toDomain() {
        return Comment.create(
                this.commentId,
                this.challengeId,
                this.userId,
                this.content,
                this.parentCommentId,
                this.replyToUserId,
                this.createdAt,
                this.updatedAt
        );
    }

    // 테스트용 인스턴스 생성 메서드
    static CommentJPAEntity testInstance(
            Long commentId,
            Long challengeId,
            Long userId,
            String content,
            Long parentCommentId,
            Long replyToUserId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        CommentJPAEntity entity = new CommentJPAEntity();
        entity.commentId = commentId;
        entity.challengeId = challengeId;
        entity.userId = userId;
        entity.content = content;
        entity.parentCommentId = parentCommentId;
        entity.replyToUserId = replyToUserId;
        entity.createdAt = createdAt;
        entity.updatedAt = updatedAt;
        return entity;
    }
}
