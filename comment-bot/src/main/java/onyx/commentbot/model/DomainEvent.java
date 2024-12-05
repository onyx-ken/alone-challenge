package onyx.commentbot.model;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime occurredOn();
}
