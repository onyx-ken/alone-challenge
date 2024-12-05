package onyx.commentbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onyx.commentbot.model.ChallengeCreatedEvent;
import onyx.commentbot.service.CommentBotService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChallengeCreatedEventConsumer {

    private final CommentBotService commentBotService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "challenge-created", groupId = "comment-auto")
    public void consume(@Payload String eventJson, @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        try {

            ChallengeCreatedEvent event = objectMapper.readValue(eventJson, ChallengeCreatedEvent.class);
            commentBotService.handleChallengeCreatedEvent(event);
            log.info("Consumed event: key={}, value={}", key, eventJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize event JSON: {}", eventJson, e);
            // 추가적인 예외 처리 로직 (예: DLQ로 전송)
        }
    }
}
