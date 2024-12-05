package onyx.challenge.domain.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentCreatedEvent implements DomainEvent{
    private Long challengeId;
    private String commentReply;
    private boolean replyable;
    private LocalDateTime occurredOn;

    public static CommentCreatedEvent create(Long challengeId, String commentReply, boolean replyable, LocalDateTime occurredOn) {
        return new CommentCreatedEvent(challengeId, commentReply, replyable, occurredOn);
    }

    public CommentCreatedEvent(Long challengeId, String commentReply, boolean replyable, LocalDateTime occurredOn) {
        this.challengeId = challengeId;
        this.commentReply = commentReply;
        this.replyable = replyable;
        this.occurredOn = occurredOn;
    }

    @Override
    public LocalDateTime occurredOn() {
        return occurredOn;
    }
}
