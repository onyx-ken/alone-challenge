package onyx.commentbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onyx.commentbot.CommentCreatedEventProducer;
import onyx.commentbot.model.ChallengeCreatedEvent;
import onyx.commentbot.model.CommentCreatedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentBotService {

    private final CommentCreatedEventProducer commentCreatedEventProducer;
    private final ObjectMapper objectMapper;

    @Value("${api.uri.comment.firstReply}")
    String botFirstReplyUri;

    public void handleChallengeCreatedEvent(ChallengeCreatedEvent event) {
        // API 응답을 CommentCreatedEvent 객체로 변환 생성
        try {
            assistantsResponseResult result = getAssistantsResponseResult(callExternalApi(event));

            CommentCreatedEvent commentCreatedEvent =
                    CommentCreatedEvent.create(event.getChallengeId(), result.commentReply(), result.isReplyable(), LocalDateTime.now());

            // Kafka에 이벤트 발행
            commentCreatedEventProducer.publishCommentCreatedEvent(commentCreatedEvent);
            log.info("Published CommentCreatedEvent: {}", commentCreatedEvent);

        } catch (Exception e) {
            log.error("Failed to process API response", e);
        }
    }

    @NotNull
    private assistantsResponseResult getAssistantsResponseResult(String apiResponse) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(apiResponse);
        String commentReply = jsonNode.get("commentReply").asText();
        boolean isReplyable = jsonNode.get("isReplyable").asBoolean();
        return new assistantsResponseResult(commentReply, isReplyable);
    }

    private record assistantsResponseResult(String commentReply, boolean isReplyable) {}

    public String callExternalApi(ChallengeCreatedEvent event) {
        try {
            // 요청 바디 생성
            ChatRequest chatRequest = new ChatRequest(formatMessage(event));

            RestClient restClient = RestClient.create();

            // POST 요청 구성 및 전송
            String responseBody = restClient.post()
                    .uri(botFirstReplyUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(chatRequest)
                    .retrieve()
                    .body(String.class);

            log.info("Received response: {}", responseBody);
            return responseBody;

        } catch (RestClientException e) {
            log.error("Error occurred while communicating with ChatGPT", e);
            return "An error occurred: " + e.getMessage();
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return "Unexpected error: " + e.getMessage();
        }
    }

    private String formatMessage(ChallengeCreatedEvent event) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userNickName", event.getUserNickName());
        data.put("startDt", event.getStartDt());
        data.put("endDt", event.getEndDt());
        data.put("target", event.getTarget());
        data.put("userAddBody", event.getUserAddBody());

        return data.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|", "", "|"));
    }

    // 요청 바디를 위한 DTO 클래스
    public class ChatRequest {
        private String content;

        public ChatRequest(String content) {
            this.content = content;
        }

        // Getter and Setter
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
