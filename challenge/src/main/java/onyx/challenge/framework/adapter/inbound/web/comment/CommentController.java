package onyx.challenge.framework.adapter.inbound.web.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.comment.CommentInputDTO;
import onyx.challenge.application.dto.comment.CommentModifyDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;
import onyx.challenge.application.port.inbound.comment.CreateCommentUseCase;
import onyx.challenge.application.port.inbound.comment.DeleteCommentUseCase;
import onyx.challenge.application.port.inbound.comment.UpdateCommentUseCase;
import onyx.challenge.framework.adapter.inbound.web.api.ApiResponse;
import onyx.challenge.framework.adapter.inbound.web.api.ApiStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges/{challengeId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CreateCommentUseCase createCommentUseCase;

    private final UpdateCommentUseCase updateCommentUseCase;

    private final DeleteCommentUseCase deleteCommentUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentOutputDTO>> addComment(
            @PathVariable Long challengeId,
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CommentCreateRequest request) {

        CommentInputDTO inputDTO = CommentInputDTO.builder()
                .challengeId(challengeId)
                .userId(userId)
                .content(request.getContent())
                .parentCommentId(request.getParentCommentId())
                .replyToUserId(request.getReplyToUserId())
                .build();

        CommentOutputDTO response = createCommentUseCase.createComment(inputDTO);
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.SUCCESS, "댓글이 등록되었습니다.", response));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentOutputDTO>> updateComment(
            @PathVariable Long challengeId,
            @PathVariable Long commentId,
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CommentUpdateRequest request) {

        CommentModifyDTO modifyDTO = CommentModifyDTO.builder()
                .commentId(commentId)
                .challengeId(challengeId)
                .userId(userId)
                .content(request.getContent())
                .build();

        CommentOutputDTO response = updateCommentUseCase.updateComment(modifyDTO);
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.ACCEPTED, "댓글이 수정되었습니다.", response));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long challengeId,
            @PathVariable Long commentId,
            @RequestHeader("X-User-Id") Long userId) {

        CommentModifyDTO modifyDTO = CommentModifyDTO.builder()
                .commentId(commentId)
                .challengeId(challengeId)
                .userId(userId)
                .build();

        deleteCommentUseCase.deleteComment(modifyDTO);
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.ACCEPTED, "댓글이 삭제되었습니다.", null));
    }
}


