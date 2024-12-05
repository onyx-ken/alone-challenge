package onyx.commentbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onyx.commentbot.model.CommentCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentCreatedEventProducer {

    private static final String TOPIC = "bot-comment-created";

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishCommentCreatedEvent(CommentCreatedEvent event) {

        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, String.valueOf(event.getChallengeId()), eventJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", event, e);
        }
    }
}
