package onyx.challenge.application.service;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.comment.CommentModifyDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;
import onyx.challenge.application.port.inbound.comment.UpdateCommentUseCase;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.domain.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateCommentService implements UpdateCommentUseCase {

    private final CommentRepository commentRepository;

    @Override
    public CommentOutputDTO updateComment(CommentModifyDTO dto) {
        // 1. 댓글 존재 여부 확인
        Comment comment = commentRepository.load(dto.getCommentId());

        // 2. 수정 권한 확인 (자신의 댓글만 수정 가능)
        if (!comment.getUserId().equals(dto.getUserId())) {
            throw new UnauthorizedException("댓글을 수정할 권한이 없습니다.");
        }
        // 3. 댓글 내용 수정
        // todo 시간 생성 등을 외부에서 입력하도록 처리하는 것을 유지하는게 맞는지 고민해 보기
        Comment updatedComment = comment.update(dto.getContent(), null);
        
        // 4. 변경 내용 실제 저장소(DB) 반영
        commentRepository.update(updatedComment);

        // 5. 결과 반환
        return CommentOutputDTO.from(updatedComment);
    }
}
