package onyx.challenge.framework.adapter.outbound.jpa.entity;

import onyx.challenge.domain.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentJPAEntityTest {
    @Test
    @DisplayName("도메인 모델에서 JPA 엔티티로 변환한 후 다시 도메인 모델로 변환하면 동일한 데이터를 유지한다")
    void givenDomainModel_whenConvertToEntityAndBack_thenDataRemainsSame() {
        // Given
        Long commentId = 1L;
        Long challengeId = 100L;
        Long userId = 200L;
        String content = "테스트 댓글입니다.";
        Long parentCommentId = null;
        Long replyToUserId = null;
        LocalDateTime createdAt = LocalDateTime.of(2024, 9, 10, 14, 20);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 9, 10, 15, 30);

        Comment originalComment = Comment.create(commentId, challengeId, userId, content,
                parentCommentId, replyToUserId, createdAt, updatedAt);

        // When
        CommentJPAEntity entity = CommentJPAEntity.fromDomain(originalComment);
        Comment convertedComment = entity.toDomain();

        // Then
        assertThat(convertedComment).isNotNull();
        assertThat(convertedComment.getCommentId()).isEqualTo(originalComment.getCommentId());
        assertThat(convertedComment.getChallengeId()).isEqualTo(originalComment.getChallengeId());
        assertThat(convertedComment.getUserId()).isEqualTo(originalComment.getUserId());
        assertThat(convertedComment.getContent()).isEqualTo(originalComment.getContent());
        assertThat(convertedComment.getParentCommentId()).isEqualTo(originalComment.getParentCommentId());
        assertThat(convertedComment.getReplyToUserId()).isEqualTo(originalComment.getReplyToUserId());
        assertThat(convertedComment.getCreatedAt()).isEqualTo(originalComment.getCreatedAt());
        assertThat(convertedComment.getUpdatedAt()).isEqualTo(originalComment.getUpdatedAt());
    }

    @Test
    @DisplayName("JPA 엔티티에서 도메인 모델로 변환할 때 모든 필드가 정확하게 매핑된다")
    void givenEntity_whenConvertToDomain_thenAllFieldsAreMappedCorrectly() {
        // Given
        CommentJPAEntity entity = CommentJPAEntity.testInstance(
                1L,
                100L,
                200L,
                "테스트 댓글입니다.",
                null,
                null,
                LocalDateTime.of(2024, 9, 10, 14, 20),
                LocalDateTime.of(2024, 9, 10, 15, 30)
        );

        // When
        Comment comment = entity.toDomain();

        // Then
        assertThat(comment).isNotNull();
        assertThat(comment.getCommentId()).isEqualTo(entity.getCommentId());
        assertThat(comment.getChallengeId()).isEqualTo(entity.getChallengeId());
        assertThat(comment.getUserId()).isEqualTo(entity.getUserId());
        assertThat(comment.getContent()).isEqualTo(entity.getContent());
        assertThat(comment.getParentCommentId()).isEqualTo(entity.getParentCommentId());
        assertThat(comment.getReplyToUserId()).isEqualTo(entity.getReplyToUserId());
        assertThat(comment.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(comment.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    @DisplayName("JPA 엔티티에서 도메인 모델로 변환할 때 null 값이 올바르게 처리된다")
    void givenEntityWithNullValues_whenConvertToDomain_thenNullValuesAreHandledCorrectly() {
        // Given
        CommentJPAEntity entity = CommentJPAEntity.testInstance(
                1L,
                100L,
                200L,
                "테스트 댓글입니다.",
                null,
                null,
                null,
                null
        );

        // When
        Comment comment = entity.toDomain();

        // Then
        assertThat(comment).isNotNull();
        assertThat(comment.getParentCommentId()).isNull();
        assertThat(comment.getReplyToUserId()).isNull();
        assertThat(comment.getCreatedAt()).isNotNull();
        assertThat(comment.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("도메인 모델에서 JPA 엔티티로 변환할 때 null 값이 올바르게 처리된다")
    void givenDomainModelWithNullValues_whenConvertToEntity_thenNullValuesAreHandledCorrectly() {
        // Given
        Comment comment = Comment.create(
                1L,
                100L,
                200L,
                "테스트 댓글입니다.",
                null,
                null,
                null,
                null
        );

        // When
        CommentJPAEntity entity = CommentJPAEntity.fromDomain(comment);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getParentCommentId()).isNull();
        assertThat(entity.getReplyToUserId()).isNull();
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
    }
}