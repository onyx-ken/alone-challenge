package onyx.challenge.framework.adapter.outbound.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import onyx.challenge.application.port.outbound.EventPublisher;
import onyx.challenge.domain.event.ChallengeCreatedEvent;
import onyx.challenge.domain.event.DomainEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
    }

    @Override
    public void publish(DomainEvent event) {
        String topic = resolveTopic(event);
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, eventJson);
            log.info("Event published: {}", eventJson);
        } catch (JsonProcessingException e) {
            // 예외 처리 로직 (로깅 등)
            e.printStackTrace();
        }
    }

    private String resolveTopic(DomainEvent event) {
        if (event instanceof ChallengeCreatedEvent) {
            return "challenge-created";
        }
        // 다른 이벤트 타입에 대한 토픽 매핑을 추가할 수 있습니다.
        return "default-topic";
    }
}
