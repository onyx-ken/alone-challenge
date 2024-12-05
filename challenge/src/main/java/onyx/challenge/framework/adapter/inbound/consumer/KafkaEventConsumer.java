package onyx.challenge.framework.adapter.inbound.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onyx.challenge.application.port.inbound.comment.BotCreateCommentUseCase;
import onyx.challenge.domain.event.CommentCreatedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile({"dev", "prod"})
@Slf4j
public class KafkaEventConsumer {

    private final BotCreateCommentUseCase botCreateCommentUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "bot-comment-created", groupId = "comment-auto")
    public void consume(String message) {
        try {
            // JSON 문자열을 CommentCreatedEvent 객체로 변환
            CommentCreatedEvent event = objectMapper.readValue(message, CommentCreatedEvent.class);
            botCreateCommentUseCase.handleEvent(event);
        } catch (Exception e) {
            // 예외 처리 및 로깅
            log.error("Failed to parse message: {}", message, e);
        }
    }
}
