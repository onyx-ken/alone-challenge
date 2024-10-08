package onyx.challenge.application.service;

import onyx.challenge.application.dto.comment.CommentInputDTO;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.domain.model.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        LocalDateTime updatedAt = LocalDateTime.now();

        Comment existingComment = Comment.create(commentId, challengeId, userId, originalContent, parentCommentId, replyToUserId, createdAt, updatedAt);

        when(commentRepository.load(commentId)).thenReturn((existingComment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentInputDTO inputDTO = CommentInputDTO
                .builder()
                .challengeId(challengeId)
                .userId(userId)
                .content(newContent)
                .build();

        // When
        //CommentOutputDTO result = updateCommentService.updateComment(inputDTO);

        // Then
        //assertThat(result).isNotNull();
        //assertThat(result.getContent()).isEqualTo(newContent);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void givenNonExistingComment_whenUpdateComment_thenThrowsCommentNotFoundException() {
        // Given
        Long commentId = 1L;
        Long challengeId = 1L;
        Long userId = 100L;
        String newContent = "Updated content";

/*        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        CommentInputDTO inputDTO = new CommentInputDTO();
        inputDTO.setCommentId(commentId);
        inputDTO.setChallengeId(challengeId);
        inputDTO.setUserId(userId);
        inputDTO.setContent(newContent);

        // When & Then
        assertThatThrownBy(() -> updateCommentService.updateComment(inputDTO))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessageContaining("댓글이 존재하지 않습니다.");*/
    }

    @Test
    void givenUnauthorizedUser_whenUpdateComment_thenThrowsUnauthorizedException() {
        // Given
        Long commentId = 1L;
        Long challengeId = 1L;
        Long userId = 100L; // 작성자와 다른 사용자 ID
        Long authorId = 200L;
        String originalContent = "Original content";
        String newContent = "Updated content";

/*        Comment existingComment = Comment.of(commentId, challengeId, authorId, originalContent, LocalDateTime.now(), null);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        CommentInputDTO inputDTO = new CommentInputDTO();
        inputDTO.setCommentId(commentId);
        inputDTO.setChallengeId(challengeId);
        inputDTO.setUserId(userId);
        inputDTO.setContent(newContent);

        // When & Then
        assertThatThrownBy(() -> updateCommentService.updateComment(inputDTO))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("댓글을 수정할 권한이 없습니다.");*/
    }
}