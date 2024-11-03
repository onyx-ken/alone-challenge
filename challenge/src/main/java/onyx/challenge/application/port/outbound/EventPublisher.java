package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.event.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
