package onyx.challenge.framework.adapter.outbound.kafka;

import onyx.challenge.application.port.outbound.EventPublisher;
import onyx.challenge.domain.event.DomainEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DomainEventPublisher {

    private final EventPublisher eventPublisher;

    public DomainEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEvent(DomainEvent event) {
        eventPublisher.publish(event);
    }
}
