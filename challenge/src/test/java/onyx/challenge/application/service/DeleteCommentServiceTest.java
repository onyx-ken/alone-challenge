package onyx.challenge.application.service;

import onyx.challenge.application.dto.comment.CommentModifyDTO;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.application.service.comment.DeleteCommentService;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private DeleteCommentService deleteCommentService;

    @Test
    @DisplayName("유효한 입력으로 댓글 삭제 시 성공한다")
    void givenValidCommentModifyDTO_whenDeleteComment_thenSuccess() {
        // Given
        Long commentId = 1L;
        Long challengeId = 100L;
        Long userId = 200L;

        Comment existingComment = Comment.create(
                commentId,
                challengeId,
                userId,
                "테스트 댓글",
                null,
                null,
                LocalDateTime.now(),
                null
        );

        Comment deletedComment = existingComment.delete(challengeId, LocalDateTime.now());

        CommentModifyDTO dto = CommentModifyDTO.builder()
                .commentId(commentId)
                .challengeId(challengeId)
                .userId(userId)
                .build();

        given(commentRepository.load(commentId)).willReturn(Optional.of(existingComment));
        given(commentRepository.save(any(Comment.class))).willReturn(deletedComment);

        // When
        deleteCommentService.deleteComment(dto);

        // Then
        verify(commentRepository, times(1)).load(commentId);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("존재하지 않는 댓글을 삭제하려고 하면 CommentNotFoundException이 발생한다")
    void givenNonExistingCommentId_whenDeleteComment_thenThrowsCommentNotFoundException() {
        // Given
        Long commentId = 1L;
        Long challengeId = 100L;
        Long userId = 200L;

        CommentModifyDTO dto = CommentModifyDTO.builder()
                .commentId(commentId)
                .challengeId(challengeId)
                .userId(userId)
                .build();

        given(commentRepository.load(commentId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> deleteCommentService.deleteComment(dto))
                .isInstanceOf(CommentNotFoundException.class);

        verify(commentRepository, times(1)).load(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    @DisplayName("권한이 없는 사용자가 댓글을 삭제하려고 하면 UnauthorizedException이 발생한다")
    void givenUnauthorizedUser_whenDeleteComment_thenThrowsUnauthorizedException() {
        // Given
        Long commentId = 1L;
        Long challengeId = 100L;
        Long commentOwnerId = 200L; // 댓글 작성자
        Long otherUserId = 300L;    // 권한이 없는 사용자

        Comment existingComment = Comment.create(
                commentId,
                challengeId,
                commentOwnerId,
                "테스트 댓글",
                null,
                null,
                LocalDateTime.now(),
                null
        );

        CommentModifyDTO dto = CommentModifyDTO.builder()
                .commentId(commentId)
                .challengeId(challengeId)
                .userId(otherUserId)
                .build();

        given(commentRepository.load(commentId)).willReturn(Optional.of(existingComment));

        // When & Then
        assertThatThrownBy(() -> deleteCommentService.deleteComment(dto))
                .isInstanceOf(UnauthorizedException.class);

        verify(commentRepository, times(1)).load(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }
}
