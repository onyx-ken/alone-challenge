package onyx.challenge.application.service;

import onyx.challenge.application.dto.comment.CommentModifyDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.application.service.comment.UpdateCommentService;
import onyx.challenge.application.service.exceptiron.UnauthorizedException;
import onyx.challenge.domain.model.Comment;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private UpdateCommentService updateCommentService;

    @Test
    void givenValidInput_whenUpdateComment_thenCommentIsUpdated() {
        // Given
        Long commentId = 1L;
        Long challengeId = 1L;
        Long userId = 100L;
        String originalContent = "Original content";
        String newContent = "Updated content";
        Long parentCommentId = null;
        Long replyToUserId = null;
        LocalDateTime createdAt = LocalDateTime.now();

        Comment existingComment = Comment.create(commentId, challengeId, userId, originalContent,
                parentCommentId, replyToUserId, createdAt, createdAt);

        when(commentRepository.load(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentModifyDTO modifyDTO = CommentModifyDTO.builder()
                .commentId(commentId)
                .userId(userId)
                .content(newContent)
                .build();

        // When
        CommentOutputDTO result = updateCommentService.updateComment(modifyDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(newContent);
        verify(commentRepository).load(commentId);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void givenUnauthorizedUser_whenUpdateComment_thenThrowsUnauthorizedException() {
        // Given
        Long commentId = 1L;
        Long challengeId = 1L;
        Long userId = 100L; // 기존 댓글 작성자 ID
        Long unauthorizedUserId = 101L; // 권한 없는 사용자 ID
        String originalContent = "Original content";
        String newContent = "Updated content";
        Long parentCommentId = null;
        Long replyToUserId = null;
        LocalDateTime createdAt = LocalDateTime.now();
        boolean isDeleted = false;

        Comment existingComment = Comment.create(commentId, challengeId, userId, originalContent,
                parentCommentId, replyToUserId, createdAt, createdAt);

        when(commentRepository.load(commentId)).thenReturn(Optional.of(existingComment));

        CommentModifyDTO modifyDTO = CommentModifyDTO.builder()
                .commentId(commentId)
                .userId(unauthorizedUserId)
                .content(newContent)
                .build();

        // When & Then
        assertThatThrownBy(() -> updateCommentService.updateComment(modifyDTO))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("댓글을 수정할 권한이 없습니다.");

        // Verify
        verify(commentRepository).load(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }
}
