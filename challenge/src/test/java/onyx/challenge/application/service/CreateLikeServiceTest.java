package onyx.challenge.application.service;

import onyx.challenge.application.dto.like.LikeInputDTO;
import onyx.challenge.application.dto.like.LikeOutputDTO;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.application.service.exceptiron.like.AlreadyLikedException;
import onyx.challenge.application.service.like.CreateLikeService;
import onyx.challenge.domain.model.Like;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CreateLikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private CreateLikeService createLikeService;

    private LikeInputDTO likeInputDTO;

    @BeforeEach
    public void setUp() {
        likeInputDTO = LikeInputDTO.builder()
                .challengeId(1L)
                .userId(100L)
                .build();
    }

    @Test
    @DisplayName("유효한 LikeInputDTO로 좋아요를 생성하면 LikeOutputDTO 를 반환한다")
    public void givenValidLikeInputDTO_whenCreateLike_thenReturnsLikeOutputDTO() {
        // Given
        Like like = Like.create(
                null,
                likeInputDTO.getChallengeId(),
                likeInputDTO.getUserId(),
                null
        );

        // 좋아요 저장 시 반환될 객체 설정
        given(likeRepository.save(any(Like.class))).willReturn(like);

        // When
        LikeOutputDTO result = createLikeService.createLike(likeInputDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChallengeId()).isEqualTo(likeInputDTO.getChallengeId());
        assertThat(result.getUserId()).isEqualTo(likeInputDTO.getUserId());
        assertThat(result.isLiked()).isTrue();
    }

    @Test
    @DisplayName("이미 좋아요를 누른 경우 AlreadyLikedException 이 발생한다")
    public void givenExistingLike_whenCreateLike_thenThrowsAlreadyLikedException() {
        // Given
        // 좋아요 저장 시 데이터베이스 예외 발생
        given(likeRepository.save(any(Like.class))).willThrow(AlreadyLikedException.class);

        // When & Then
        assertThatThrownBy(() -> createLikeService.createLike(likeInputDTO))
                .isInstanceOf(AlreadyLikedException.class);
    }

}
