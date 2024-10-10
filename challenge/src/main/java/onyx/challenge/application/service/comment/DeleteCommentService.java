package onyx.challenge.application.service.comment;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.comment.CommentModifyDTO;
import onyx.challenge.application.port.inbound.comment.DeleteCommentUseCase;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.application.service.exceptiron.UnauthorizedException;
import onyx.challenge.application.service.exceptiron.comment.CommentNotFoundException;
import onyx.challenge.domain.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteCommentService implements DeleteCommentUseCase {

    private final CommentRepository commentRepository;

    @Override
    public void deleteComment(CommentModifyDTO dto) {
        // 댓글 존재 여부 확인
        Comment comment = commentRepository.load(dto.getCommentId())
                .orElseThrow(() -> new CommentNotFoundException(dto.getCommentId()));
        // 수정 권한 확인 (자신의 댓글만 수정 가능) (애플리케이션 계층에서의 역할)
        if (!comment.getUserId().equals(dto.getUserId())) {
            throw new UnauthorizedException("댓글을 수정할 권한이 없습니다.");
        }
        // 삭제 메서드 실행 (함수형)
        Comment deltedComment = comment.delete(dto.getChallengeId(), null);
        // DB 반영 (Soft Delete)
        commentRepository.save(deltedComment);
    }
}
