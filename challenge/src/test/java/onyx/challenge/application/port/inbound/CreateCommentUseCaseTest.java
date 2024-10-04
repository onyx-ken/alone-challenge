package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.comment.CommentInputDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.application.service.CreateCommentService;
import onyx.challenge.domain.model.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CreateCommentUseCaseTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CreateCommentService createCommentService;

    @Test
    @DisplayName("유효한 입력으로 댓글 생성 시 CommentOutputDTO를 반환한다")
    void givenValidCommentInputDTO_whenCreateComment_thenReturnsCommentOutputDTO() {
        // Given
        CommentInputDTO inputDTO = CommentInputDTO.builder()
                .challengeId(1L)
                .userId(100L)
                .content("테스트 댓글입니다.")
                .build();

        Comment comment = Comment.create(null, inputDTO.getChallengeId(), inputDTO.getUserId(),
                inputDTO.getContent(), null, null, null, null);

        Comment savedComment = Comment.create(1L, comment.getChallengeId(), comment.getUserId(),
                comment.getContent(), comment.getParentCommentId(), comment.getReplyToUserId(),
                comment.getCreatedAt(), comment.getUpdatedAt());

        given(commentRepository.save(any(Comment.class))).willReturn(savedComment);

        // When
        CommentOutputDTO result = createCommentService.createComment(inputDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCommentId()).isEqualTo(1L);
        assertThat(result.getChallengeId()).isEqualTo(inputDTO.getChallengeId());
        assertThat(result.getUserId()).isEqualTo(inputDTO.getUserId());
        assertThat(result.getContent()).isEqualTo(inputDTO.getContent());
    }

    @Test
    @DisplayName("대댓글 생성 시 CommentOutputDTO를 반환한다")
    void givenValidReplyInputDTO_whenCreateComment_thenReturnsCommentOutputDTO() {
        // Given
        CommentInputDTO inputDTO = CommentInputDTO.builder()
                .challengeId(1L)
                .userId(101L)
                .content("대댓글입니다.")
                .parentCommentId(10L)
                .build();

        Comment comment = Comment.create(null, inputDTO.getChallengeId(), inputDTO.getUserId(),
                inputDTO.getContent(), inputDTO.getParentCommentId(), null, null, null);

        Comment savedComment = Comment.create(2L, comment.getChallengeId(), comment.getUserId(),
                comment.getContent(), comment.getParentCommentId(), comment.getReplyToUserId(),
                comment.getCreatedAt(), comment.getUpdatedAt());

        given(commentRepository.save(any(Comment.class))).willReturn(savedComment);

        // When
        CommentOutputDTO result = createCommentService.createComment(inputDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCommentId()).isEqualTo(2L);
        assertThat(result.getParentCommentId()).isEqualTo(inputDTO.getParentCommentId());
    }
}