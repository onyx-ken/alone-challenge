package onyx.challenge.framework.adapter.inbound.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.FileData;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.port.outbound.CommentRepository;
import onyx.challenge.domain.event.CommentCreatedEvent;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.model.Comment;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.ImageType;
import onyx.challenge.domain.vo.Period;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = { "bot-comment-created" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class KafkaEventConsumerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testKafkaEventConsumer() throws Exception {
        // Given
        // 1. 사전에 필요한 챌린지 데이터를 생성
        Challenge challenge = createTestChallenge();

        // 2. CommentCreatedEvent 생성
        CommentCreatedEvent event = new CommentCreatedEvent(
                challenge.getChallengeId(), // 생성된 챌린지의 ID 사용
                "This is a bot comment",
                true, // isReplyable
                LocalDateTime.now()
        );

        // When
        // 이벤트를 Kafka 토픽에 발행
        objectMapper.registerModule(new JavaTimeModule());
        String eventJson = objectMapper.writeValueAsString(event);
        kafkaTemplate.send("bot-comment-created", String.valueOf(event.getChallengeId()), eventJson);
        kafkaTemplate.flush();

        // Then
        // Awaitility를 사용하여 댓글이 저장될 때까지 대기
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            List<Comment> comments = commentRepository.findAll();
            assertThat(comments).hasSize(1);
            Comment savedComment = comments.getFirst();
            assertThat(savedComment.getChallengeId()).isEqualTo(challenge.getChallengeId());
            assertThat(savedComment.getContent()).isEqualTo("This is a bot comment");
            assertThat(savedComment.getUserId()).isEqualTo(0L); // 봇의 userId가 0L인 경우
        });
    }

    private Challenge createTestChallenge() {
        // 필요한 데이터 설정
        FileData fileData = FileData.create(
                "test-image.jpg",
                "test data".getBytes(StandardCharsets.UTF_8),
                "image/jpeg"
        );

        List<ChallengeInputDTO.ImageData> attachedImages = new ArrayList<>();
        attachedImages.add(new ChallengeInputDTO.ImageData(
                fileData, 1, ImageType.USER_UPLOAD
        ));

        ChallengeInputDTO challengeInputDTO = ChallengeInputDTO.builder()
                .userId(1L)
                .nickName("JohnDoe")
                .startDate(LocalDate.of(2024, 9, 19))
                .endDate(LocalDate.of(2024, 9, 25))
                .mainContent("Run 5K")
                .additionalContent("Do It!")
                .goalType("POSITIVE")
                .attachedImages(attachedImages)
                .build();

        Long mockImageId = 100L;

        Challenge challenge = Challenge.create(
                1L,
                challengeInputDTO.getUserId(),
                challengeInputDTO.getNickName(),
                new Period(challengeInputDTO.getStartDate(), challengeInputDTO.getEndDate()),
                GoalContent.create(
                        challengeInputDTO.getMainContent(),
                        challengeInputDTO.getAdditionalContent(),
                        GoalType.valueOf(challengeInputDTO.getGoalType())
                ),
                List.of(mockImageId)
        );
        // 챌린지를 데이터베이스에 저장
        return challengeRepository.save(challenge);
    }
}
