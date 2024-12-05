package onyx.challenge.application.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onyx.challenge.application.port.inbound.comment.BotCreateCommentUseCase;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.domain.event.CommentCreatedEvent;
import onyx.challenge.domain.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BotCreateCommentService implements BotCreateCommentUseCase {

    private final CommentRepository commentRepository;

    @Override
    public void handleEvent(CommentCreatedEvent event) {
        log.info("Handling event {}", event);
        if (event.isReplyable()) {
            commentRepository.save(Comment.create(
                    null,
                    event.getChallengeId(),
                    getBotUserId(),
                    event.getCommentReply(),
                    null,
                    null,
                    null,
                    null
            ));
        }
    }

    private Long getBotUserId() {
        return 0L;
    }
}
