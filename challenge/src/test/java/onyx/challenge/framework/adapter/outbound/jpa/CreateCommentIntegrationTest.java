package onyx.challenge.framework.adapter.outbound.jpa;

import onyx.challenge.application.dto.comment.CommentInputDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.service.comment.CreateCommentService;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.Period;
import onyx.challenge.framework.adapter.outbound.jpa.comment.CommentJpaRepository;
import onyx.challenge.framework.adapter.outbound.jpa.entity.CommentJPAEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CreateCommentIntegrationTest {

    @Autowired
    private CreateCommentService createCommentService;

    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @BeforeAll
    static void setUp(@Autowired ChallengeRepository challengeRepository) {
        // 댓글 생성 이전에 챌린지가 등록되어 있어야 한다. (제약조건)
        Challenge challenge = Challenge.create(
                null,
                1L,
                "JohnDoe",
                new Period(LocalDate.of(2024, 9, 19), LocalDate.of(2024, 9, 21)),
                GoalContent.create("Run 5K", "Do It!", GoalType.POSITIVE),
                Arrays.asList(5L, 6L)
        );
        challengeRepository.save(challenge);
    }

    @Test
    @DisplayName("유효한 입력으로 댓글 생성 시 데이터베이스에 저장된다")
    void givenValidCommentInputDTO_whenCreateComment_thenCommentIsPersistedInDatabase() {
        // Given
        CommentInputDTO inputDTO = CommentInputDTO.builder()
                .challengeId(1L)
                .userId(100L)
                .content("테스트 댓글입니다.")
                .build();

        // When
        CommentOutputDTO outputDTO = createCommentService.createComment(inputDTO);

        // Then
        assertThat(outputDTO).isNotNull();
        assertThat(outputDTO.getCommentId()).isNotNull();
        assertThat(outputDTO.getChallengeId()).isEqualTo(inputDTO.getChallengeId());
        assertThat(outputDTO.getUserId()).isEqualTo(inputDTO.getUserId());
        assertThat(outputDTO.getContent()).isEqualTo(inputDTO.getContent());

        // 데이터베이스에 저장되었는지 확인
        CommentJPAEntity savedEntity = commentJpaRepository.findById(outputDTO.getCommentId()).orElse(null);
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getChallengeId()).isEqualTo(inputDTO.getChallengeId());
        assertThat(savedEntity.getUserId()).isEqualTo(inputDTO.getUserId());
        assertThat(savedEntity.getContent()).isEqualTo(inputDTO.getContent());
    }

    @Test
    @DisplayName("유효한 입력으로 대댓글 생성 시 데이터베이스에 저장된다")
    void givenValidReplyInputDTO_whenCreateComment_thenReplyIsPersistedInDatabase() {
        // Given
        // 먼저 최상위 댓글을 생성
        CommentInputDTO parentInputDTO = CommentInputDTO.builder()
                .challengeId(1L)
                .userId(100L)
                .content("최상위 댓글입니다.")
                .build();

        CommentOutputDTO parentOutputDTO = createCommentService.createComment(parentInputDTO);

        // 대댓글 생성
        CommentInputDTO replyInputDTO = CommentInputDTO.builder()
                .challengeId(1L)
                .userId(101L)
                .content("대댓글입니다.")
                .parentCommentId(parentOutputDTO.getCommentId())
                .replyToUserId(100L)
                .build();

        // When
        CommentOutputDTO replyOutputDTO = createCommentService.createComment(replyInputDTO);

        // Then
        assertThat(replyOutputDTO).isNotNull();
        assertThat(replyOutputDTO.getCommentId()).isNotNull();
        assertThat(replyOutputDTO.getParentCommentId()).isEqualTo(parentOutputDTO.getCommentId());

        // 데이터베이스에 저장되었는지 확인
        CommentJPAEntity savedEntity = commentJpaRepository.findById(replyOutputDTO.getCommentId()).orElse(null);
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getParentCommentId()).isEqualTo(parentOutputDTO.getCommentId());
    }

    @Test
    @DisplayName("필수 필드가 없는 경우 예외가 발생한다")
    void givenInvalidInputDTO_whenCreateComment_thenThrowsException() {
        // Given
        CommentInputDTO inputDTO = CommentInputDTO.builder()
                .challengeId(null)
                .userId(null)
                .content("")
                .build();

        // When & Then
        assertThatThrownBy(() -> createCommentService.createComment(inputDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("챌린지 ID는 필수입니다.");
    }
}
