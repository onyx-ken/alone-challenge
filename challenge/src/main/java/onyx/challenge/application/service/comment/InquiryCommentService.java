package onyx.challenge.application.service.comment;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.comment.CommentOutputDTO;
import onyx.challenge.application.port.inbound.comment.InquiryCommentUseCase;
import onyx.challenge.application.port.outbound.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryCommentService implements InquiryCommentUseCase {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentOutputDTO> getCommentsByChallengeId(Long challengeId) {
        return commentRepository.getListByChallengeId(challengeId).stream().map(CommentOutputDTO::from).toList();
    }
}
