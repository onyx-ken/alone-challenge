package onyx.challenge.framework.adapter.inbound.web.like;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.like.LikeInputDTO;
import onyx.challenge.application.dto.like.LikeOutputDTO;
import onyx.challenge.application.port.inbound.like.CreateLikeUseCase;
import onyx.challenge.application.port.inbound.like.DeleteLikeUseCase;
import onyx.challenge.framework.adapter.inbound.web.api.ApiResponse;
import onyx.challenge.framework.adapter.inbound.web.api.ApiStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges/{challengeId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final CreateLikeUseCase createLikeUseCase;

    private final DeleteLikeUseCase deleteLikeUseCase;

    @PostMapping
    ResponseEntity<ApiResponse<LikeOutputDTO>> addLike(@PathVariable Long challengeId,
                                                       @RequestHeader("X-User-Id") Long userId) {
        LikeOutputDTO response = createLikeUseCase.createLike(new LikeInputDTO(challengeId, userId));
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.SUCCESS, "좋아요가 등록되었습니다..", response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Boolean>> removeLike(
            @PathVariable Long challengeId,
            @RequestHeader("X-User-Id") Long userId) {
        deleteLikeUseCase.deleteLike(new LikeInputDTO(challengeId, userId));
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.ACCEPTED, "좋아요가 취소되었습니다.", true));
    }
}
