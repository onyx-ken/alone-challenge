package onyx.challenge.framework.adapter.outbound.kafka;

import lombok.extern.slf4j.Slf4j;
import onyx.challenge.application.port.outbound.EventPublisher;
import onyx.challenge.domain.event.ChallengeCreatedEvent;
import onyx.challenge.domain.event.DomainEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@Slf4j
public class LocalKafkaEventPublisher implements EventPublisher {

    @Override
    public void publish(DomainEvent event) {
        log.info("This Event published For Local And Do nothing!");
    }

    private String resolveTopic(DomainEvent event) {
        if (event instanceof ChallengeCreatedEvent) {
            return "challenge-created";
        }
        // 다른 이벤트 타입에 대한 토픽 매핑을 추가할 수 있습니다.
        return "default-topic";
    }
}
