package onyx.challenge.application.service;

import onyx.challenge.application.dto.comment.CommentModifyDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.application.service.comment.UpdateCommentService;
import onyx.challenge.application.service.exceptiron.UnauthorizedException;
import onyx.challenge.application.service.exceptiron.comment.CommentNotFoundException;
import onyx.challenge.domain.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private UpdateCommentService updateCommentService;

    @Test
    @DisplayName("유효한 입력으로 댓글 수정 시 CommentOutputDTO를 반환한다")
    void givenValidCommentModifyDTO_whenUpdateComment_thenReturnsCommentOutputDTO() {
        // Given
        Long commentId = 1L;
        Long challengeId = 100L;
        Long userId = 200L;
        String newContent = "수정된 댓글 내용";

        Comment existingComment = Comment.create(
                commentId,
                challengeId,
                userId,
                "기존 댓글 내용",
                null,
                null,
                LocalDateTime.now(),
                null
        );

        Comment updatedComment = existingComment.update(newContent, challengeId, LocalDateTime.now());

        CommentModifyDTO dto = CommentModifyDTO.builder()
                .commentId(commentId)
                .challengeId(challengeId)
                .userId(userId)
                .content(newContent)
                .build();

        given(commentRepository.load(commentId)).willReturn(Optional.of(existingComment));
        given(commentRepository.save(any(Comment.class))).willReturn(updatedComment);

        // When
        CommentOutputDTO result = updateCommentService.updateComment(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCommentId()).isEqualTo(commentId);
        assertThat(result.getContent()).isEqualTo(newContent);

        verify(commentRepository, times(1)).load(commentId);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("존재하지 않는 댓글을 수정하려고 하면 CommentNotFoundException이 발생한다")
    void givenNonExistingCommentId_whenUpdateComment_thenThrowsCommentNotFoundException() {
        // Given
        Long commentId = 1L;
        Long challengeId = 100L;
        Long userId = 200L;
        String newContent = "수정된 댓글 내용";

        CommentModifyDTO dto = CommentModifyDTO.builder()
                .commentId(commentId)
                .challengeId(challengeId)
                .userId(userId)
                .content(newContent)
                .build();

        given(commentRepository.load(commentId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> updateCommentService.updateComment(dto))
                .isInstanceOf(CommentNotFoundException.class);

        verify(commentRepository, times(1)).load(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    @DisplayName("권한이 없는 사용자가 댓글을 수정하려고 하면 UnauthorizedException이 발생한다")
    void givenUnauthorizedUser_whenUpdateComment_thenThrowsUnauthorizedException() {
        // Given
        Long commentId = 1L;
        Long challengeId = 100L;
        Long commentOwnerId = 200L;
        Long otherUserId = 300L;
        String newContent = "수정된 댓글 내용";

        Comment existingComment = Comment.create(
                commentId,
                challengeId,
                commentOwnerId,
                "기존 댓글 내용",
                null,
                null,
                LocalDateTime.now(),
                null
        );

        CommentModifyDTO dto = CommentModifyDTO.builder()
                .commentId(commentId)
                .challengeId(challengeId)
                .userId(otherUserId)
                .content(newContent)
                .build();

        given(commentRepository.load(commentId)).willReturn(Optional.of(existingComment));

        // When & Then
        assertThatThrownBy(() -> updateCommentService.updateComment(dto))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("댓글을 수정할 권한이 없습니다.");

        verify(commentRepository, times(1)).load(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    @DisplayName("삭제된 댓글을 수정하려고 하면 IllegalStateException이 발생한다")
    void givenDeletedComment_whenUpdateComment_thenThrowsIllegalStateException() {
        // Given
        Long commentId = 1L;
        Long challengeId = 100L;
        Long userId = 200L;
        String newContent = "수정된 댓글 내용";

        Comment existingComment = Comment.create(
                commentId,
                challengeId,
                userId,
                "기존 댓글 내용",
                null,
                null,
                LocalDateTime.now(),
                null
        ).delete(challengeId, LocalDateTime.now());

        CommentModifyDTO dto = CommentModifyDTO.builder()
                .commentId(commentId)
                .challengeId(challengeId)
                .userId(userId)
                .content(newContent)
                .build();

        given(commentRepository.load(commentId)).willReturn(Optional.of(existingComment));

        // When & Then
        assertThatThrownBy(() -> updateCommentService.updateComment(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("삭제된 댓글은 수정할 수 없습니다.");

        verify(commentRepository, times(1)).load(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }
}
