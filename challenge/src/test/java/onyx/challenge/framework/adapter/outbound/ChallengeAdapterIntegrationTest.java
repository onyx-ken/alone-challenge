package onyx.challenge.framework.adapter.outbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;
import onyx.challenge.application.dto.FileData;
import onyx.challenge.application.port.inbound.CreateChallengeUseCase;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.service.CreateChallengeService;
import onyx.challenge.domain.event.ChallengeCreatedEvent;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.ImageType;
import onyx.challenge.domain.vo.Period;
import onyx.challenge.framework.adapter.outbound.jpa.ChallengeJpaRepository;
import onyx.challenge.framework.adapter.outbound.jpa.entity.ChallengeJPAEntity;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = { "challenge-created" })
class ChallengeAdapterIntegrationTest {

    @Autowired
    private CreateChallengeUseCase createChallengeService;

    @Autowired
    private ChallengeJpaRepository challengeJPARepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void givenChallengeInputDTO_whenCreateChallenge_thenChallengeIsPersistedInDatabaseAndEventIsPublished() throws Exception {
        // Kafka 컨슈머 설정
        Map<String, Object> consumerProps = new HashMap<>(KafkaTestUtils.consumerProps("testGroup", "false", embeddedKafkaBroker));
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, String> consumer = consumerFactory.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "challenge-created");

        // TransactionTemplate 사용
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        // 트랜잭션 내에서 작업 수행
        transactionTemplate.executeWithoutResult(status -> {
            // Given
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

            // When
            ChallengeOutputDTO result = createChallengeService.createChallenge(challengeInputDTO);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getChallengeId()).isNotNull();
            assertThat(result.getLikeCount()).isEqualTo(0);

            // 데이터베이스 검증
            ChallengeJPAEntity foundEntity = challengeJPARepository.findById(result.getChallengeId()).orElse(null);
            assertThat(foundEntity).isNotNull();
            assertThat(foundEntity.getUserId()).isEqualTo(1L);
            assertThat(foundEntity.getNickName()).isEqualTo("JohnDoe");
            assertThat(foundEntity.getGoalContent().getMainContent()).isEqualTo("Run 5K");
        });

        // 트랜잭션이 커밋된 후에 이벤트가 발행되었으므로, 이제 Kafka에서 메시지를 소비합니다.
        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records.count()).isGreaterThan(0);

        // 이벤트 내용 검증
        String eventJson = records.iterator().next().value();
        objectMapper.registerModule(new JavaTimeModule());
        ChallengeCreatedEvent event = objectMapper.readValue(eventJson, ChallengeCreatedEvent.class);

        assertThat(event.getChallengeId()).isNotNull();
        assertThat(event.getUserId()).isEqualTo(1L);

        consumer.close();
    }
}
