package onyx.challenge.domain.model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}