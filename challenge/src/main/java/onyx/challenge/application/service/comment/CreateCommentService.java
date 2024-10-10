package onyx.challenge.application.service.comment;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.comment.CommentInputDTO;
import onyx.challenge.application.dto.comment.CommentOutputDTO;
import onyx.challenge.application.port.inbound.comment.CreateCommentUseCase;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.domain.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateCommentService implements CreateCommentUseCase {

    private final CommentRepository commentRepository;

    @Override
    public CommentOutputDTO createComment(CommentInputDTO commentInputDTO) {

        Comment comment = Comment.create(
                null,
                commentInputDTO.getChallengeId(),
                commentInputDTO.getUserId(),
                commentInputDTO.getContent(),
                commentInputDTO.getParentCommentId(),
                commentInputDTO.getReplyToUserId(),
                null,
                null
        );

        return CommentOutputDTO.from(commentRepository.save(comment));
    }
}
