package onyx.challenge.application.service.comment;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.comment.CommentModifyDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;
import onyx.challenge.application.port.inbound.comment.UpdateCommentUseCase;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.application.service.exceptiron.UnauthorizedException;
import onyx.challenge.application.service.exceptiron.comment.CommentNotFoundException;
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

        // 댓글 존재 여부 확인
        Comment comment = commentRepository.load(dto.getCommentId())
                .orElseThrow(() -> new CommentNotFoundException(dto.getCommentId()));
        // 수정 권한 확인 (자신의 댓글만 수정 가능) (애플리케이션 계층에서의 역할)
        if (!comment.getUserId().equals(dto.getUserId())) {
            throw new UnauthorizedException("댓글을 수정할 권한이 없습니다.");
        }
        // 댓글 내용 수정(변경된 새로운 객체 생성- 함수형 스타일)
        // todo 시간 생성 등을 외부에서 입력하도록 처리하는 것을 유지하는게 맞는지 고민해 보기
        Comment updatedComment = comment.update(dto.getContent(), dto.getChallengeId() ,null);

        // 변경 내용 실제 저장소(DB) 반영 & 결과 반환
        return CommentOutputDTO.from(commentRepository.save(updatedComment));
    }
}
