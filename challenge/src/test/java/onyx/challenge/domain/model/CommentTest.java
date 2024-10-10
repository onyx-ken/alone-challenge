package onyx.challenge.domain.model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentTest {

    @Test
    @DisplayName("유효한 데이터로 Comment를 생성하면 성공한다")
    void whenCreatingCommentWithValidData_thenSuccess() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String content = "테스트 댓글입니다.";
        Long parentCommentId = null;
        Long replyToUserId = null;

        // When
        Comment comment = Comment.create(null, challengeId, userId, content,
                parentCommentId, replyToUserId, null, null);

        // Then
        assertThat(comment).isNotNull();
        assertThat(comment.getChallengeId()).isEqualTo(challengeId);
        assertThat(comment.getUserId()).isEqualTo(userId);
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getParentCommentId()).isNull();
        assertThat(comment.getReplyToUserId()).isNull();
        assertThat(comment.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("댓글 생성 시 isDeleted 필드는 false여야 한다")
    void whenCreatingComment_thenIsDeletedIsFalse() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String content = "테스트 댓글입니다.";

        // When
        Comment comment = Comment.create(null, challengeId, userId, content,
                null, null, null, null);

        // Then
        assertThat(comment.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("최상위 댓글에 대한 대댓글 생성 시 성공한다")
    void whenCreatingReplyToTopLevelComment_thenSuccess() {
        // Given
        Long challengeId = 1L;
        Long userId = 101L;
        String content = "대댓글입니다.";
        Long parentCommentId = 10L; // 최상위 댓글 ID
        Long replyToUserId = null;

        // When
        Comment replyComment = Comment.create(null, challengeId, userId, content,
                parentCommentId, replyToUserId, null, null);

        // Then
        assertThat(replyComment).isNotNull();
        assertThat(replyComment.getParentCommentId()).isEqualTo(parentCommentId);
        assertThat(replyComment.getReplyToUserId()).isNull();
        assertThat(replyComment.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("대댓글에 대한 답글 생성 시 성공한다")
    void whenCreatingReplyToReply_thenSuccess() {
        // Given
        Long challengeId = 1L;
        Long userId = 102L;
        String content = "대댓글의 답글입니다.";
        Long parentCommentId = 10L; // 최상위 댓글 ID
        Long replyToUserId = 101L; // 답글 대상 사용자 ID

        // When
        Comment replyToReplyComment = Comment.create(null, challengeId, userId, content,
                parentCommentId, replyToUserId, null, null);

        // Then
        assertThat(replyToReplyComment).isNotNull();
        assertThat(replyToReplyComment.getParentCommentId()).isEqualTo(parentCommentId);
        assertThat(replyToReplyComment.getReplyToUserId()).isEqualTo(replyToUserId);
        assertThat(replyToReplyComment.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("필수 필드가 없을 경우 IllegalArgumentException이 발생한다")
    void whenCreatingCommentWithInvalidData_thenThrowsException() {
        // Given
        Long challengeId = null;
        Long userId = null;
        String content = "";

        // When & Then
        assertThatThrownBy(() -> Comment.create(null, challengeId, userId, content,
                null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("챌린지 ID는 필수입니다.");
    }

    @Test
    @DisplayName("댓글을 성공적으로 수정한다")
    void whenUpdatingComment_thenSuccess() {
        // Given
        Long commentId = 1L;
        Long challengeId = 1L;
        Long userId = 100L;
        String originalContent = "원본 댓글 내용";
        String newContent = "수정된 댓글 내용";
        LocalDateTime createdAt = LocalDateTime.now();

        Comment comment = Comment.create(commentId, challengeId, userId, originalContent,
                null, null, createdAt, null);

        // When
        Comment updatedComment = comment.update(newContent, challengeId, LocalDateTime.now());

        // Then
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getContent()).isEqualTo(newContent);
        assertThat(updatedComment.getUpdatedAt()).isNotNull();
        assertThat(updatedComment.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("삭제된 댓글을 수정하려고 하면 예외가 발생한다")
    void whenUpdatingDeletedComment_thenThrowsException() {
        // Given
        Long commentId = 1L;
        Long challengeId = 1L;
        Long userId = 100L;
        String originalContent = "원본 댓글 내용";
        String newContent = "수정된 댓글 내용";
        LocalDateTime createdAt = LocalDateTime.now();

        Comment comment = Comment.create(commentId, challengeId, userId, originalContent,
                null, null, createdAt, null);

        // 댓글 삭제
        Comment deletedComment = comment.delete(challengeId, LocalDateTime.now());

        // When & Then
        assertThatThrownBy(() -> deletedComment.update(newContent, challengeId, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("삭제된 댓글은 수정할 수 없습니다.");
    }

    @Test
    @DisplayName("댓글을 성공적으로 삭제한다")
    void whenDeletingComment_thenSuccess() {
        // Given
        Long commentId = 1L;
        Long challengeId = 1L;
        Long userId = 100L;
        String content = "삭제할 댓글 내용";
        LocalDateTime createdAt = LocalDateTime.now();

        Comment comment = Comment.create(commentId, challengeId, userId, content,
                null, null, createdAt, null);

        // When
        Comment deletedComment = comment.delete(challengeId, LocalDateTime.now());

        // Then
        assertThat(deletedComment).isNotNull();
        assertThat(deletedComment.isDeleted()).isTrue();
        assertThat(deletedComment.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("이미 삭제된 댓글을 다시 삭제하려고 하면 예외가 발생한다")
    void whenDeletingAlreadyDeletedComment_thenThrowsException() {
        // Given
        Long commentId = 1L;
        Long challengeId = 1L;
        Long userId = 100L;
        String content = "삭제할 댓글 내용";
        LocalDateTime createdAt = LocalDateTime.now();

        Comment comment = Comment.create(commentId, challengeId, userId, content,
                null, null, createdAt, null);

        // 첫 번째 삭제
        Comment deletedComment = comment.delete(challengeId, LocalDateTime.now());

        // When & Then
        assertThatThrownBy(() -> deletedComment.delete(challengeId, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 삭제된 댓글 입니다.");
    }
}
