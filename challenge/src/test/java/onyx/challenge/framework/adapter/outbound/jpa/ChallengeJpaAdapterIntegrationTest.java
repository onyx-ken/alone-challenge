package onyx.challenge.framework.adapter.outbound.jpa;

import onyx.challenge.application.port.outbound.CreateChallengeOutputPort;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.Period;
import onyx.challenge.framework.adapter.outbound.jpa.entity.ChallengeJPAEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChallengeJpaAdapterIntegrationTest {

    @Autowired
    private CreateChallengeOutputPort createChallengeOutputPort;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Test
    public void givenChallenge_whenSave_thenChallengeIsPersistedInDatabase() {
        // Given
        Challenge challenge = Challenge.create(
                null,
                1L,
                "JohnDoe",
                new Period(LocalDate.of(2024,9,19), LocalDate.of(2024, 9 ,21)),
                GoalContent.create("Run 5K", "Do It!", GoalType.POSITIVE),
                List.of("path/to/image1.jpg", "path/to/image2.jpg"),
                "path/to/certificate.jpg"
        );

        // When
        Challenge savedChallenge = createChallengeOutputPort.save(challenge);

        // Then
        assertThat(savedChallenge).isNotNull();
        assertThat(savedChallenge.getNickName()).isEqualTo("JohnDoe");

        // Verify that the challenge is persisted in the database
        ChallengeJPAEntity foundEntity = challengeRepository.findById(savedChallenge.getChallengeId()).orElse(null);
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getUserId()).isEqualTo(1L);
        assertThat(foundEntity.getNickName()).isEqualTo("JohnDoe");
        assertThat(foundEntity.getGoalContent().getMainContent()).isEqualTo("Run 5K");
    }
}