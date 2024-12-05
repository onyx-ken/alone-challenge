package onyx.challenge.framework.adapter.inbound.web.like;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.like.LikeInputDTO;
import onyx.challenge.application.dto.like.LikeOutputDTO;
import onyx.challenge.application.port.inbound.like.CreateLikeUseCase;
import onyx.challenge.application.port.inbound.like.DeleteLikeUseCase;
import onyx.challenge.application.port.inbound.like.InquiryLikedChallengeIdUseCase;
import onyx.challenge.framework.adapter.inbound.web.api.ApiResponse;
import onyx.challenge.framework.adapter.inbound.web.api.ApiStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final CreateLikeUseCase createLikeUseCase;

    private final DeleteLikeUseCase deleteLikeUseCase;

    private final InquiryLikedChallengeIdUseCase inquiryLikedChallengeIdUseCase;

    @PostMapping("/challenges/{challengeId}/likes")
    public ResponseEntity<ApiResponse<LikeOutputDTO>> addLike(@PathVariable Long challengeId,
                                                       @RequestHeader("X-User-Id") Long userId) {
        LikeOutputDTO response = createLikeUseCase.createLike(new LikeInputDTO(challengeId, userId));
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.SUCCESS, "좋아요가 등록되었습니다..", response));
    }

    @DeleteMapping("/challenges/{challengeId}/likes")
    public ResponseEntity<ApiResponse<Boolean>> removeLike(
            @PathVariable Long challengeId,
            @RequestHeader("X-User-Id") Long userId) {
        deleteLikeUseCase.deleteLike(new LikeInputDTO(challengeId, userId));
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.ACCEPTED, "좋아요가 취소되었습니다.", true));
    }

    @PostMapping("/challenges/likes/check")
    public ResponseEntity<ApiResponse<List<String>>> checkLikesListByChallengeId(
            @RequestBody InquiryLikedChallengeIdRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        List<Long> likedChallengeIds =
                inquiryLikedChallengeIdUseCase.getLikedChallengeIds(request.getChallengeIds()
                        .stream().map(Long::valueOf).toList(), userId);
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.ACCEPTED, "좋아요를 누른 ID 목록을 반환합니다.",
                likedChallengeIds.stream().map(Objects::toString).toList()));
    }
}
