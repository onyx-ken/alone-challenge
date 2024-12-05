package onyx.challenge.application.port.inbound.comment;

import onyx.challenge.domain.event.CommentCreatedEvent;

public interface BotCreateCommentUseCase {
    void handleEvent(CommentCreatedEvent event);
}
