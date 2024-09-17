package onyx.challenge.entity;

import onyx.challenge.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChallengeConversionTest {

    @Test
    @DisplayName("도메인 모델에서 JPA 엔티티로 변환한 후 다시 도메인 모델로 변환하면 동일한 데이터를 유지한다")
    void givenDomainModel_whenConvertToEntityAndBack_thenDataRemainsSame() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String nickName = "챌린저";
        Period period = new Period(LocalDate.now(), LocalDate.now().plusDays(7));
        GoalContent goalContent = GoalContent.from("목표 내용", "추가 내용", GoalType.POSITIVE);
        List<String> attachedImagePaths = Arrays.asList("image1.png", "image2.png");
        String challengeCertificateImagePath = "certificate.png";
        List<String> additionalInfoAttachedImagePaths = List.of("additional_image1.png");
        String comments = "추가 정보 코멘트";
        AdditionalInfo additionalInfo = AdditionalInfo.from(additionalInfoAttachedImagePaths, comments);
        ChallengeResult challengeResult = ChallengeResult.from(
                ChallengeResultStatus.SUCCEEDED,
                LocalDate.now().minusDays(1),
                additionalInfo
        );

        Challenge originalChallenge = Challenge.from(
                challengeId,
                userId,
                nickName,
                period,
                goalContent,
                attachedImagePaths,
                challengeCertificateImagePath,
                challengeResult
        );

        // When
        ChallengeJPAEntity challengeEntity = ChallengeJPAEntity.fromDomain(originalChallenge);
        Challenge convertedChallenge = challengeEntity.toDomain();

        // Then
        assertThat(convertedChallenge).isNotNull();
        assertThat(convertedChallenge.getChallengeId()).isEqualTo(originalChallenge.getChallengeId());
        assertThat(convertedChallenge.getUserId()).isEqualTo(originalChallenge.getUserId());
        assertThat(convertedChallenge.getNickName()).isEqualTo(originalChallenge.getNickName());
        assertThat(convertedChallenge.getPeriod()).isEqualTo(originalChallenge.getPeriod());
        assertThat(convertedChallenge.getGoalContent()).isEqualTo(originalChallenge.getGoalContent());
        assertThat(convertedChallenge.getAttachedImagePaths()).isEqualTo(originalChallenge.getAttachedImagePaths());
        assertThat(convertedChallenge.getChallengeCertificateImagePath()).isEqualTo(originalChallenge.getChallengeCertificateImagePath());
        assertThat(convertedChallenge.getResult().getStatus()).isEqualTo(originalChallenge.getResult().getStatus());
        assertThat(convertedChallenge.getResult().getConfirmedDate()).isEqualTo(originalChallenge.getResult().getConfirmedDate());
        assertThat(convertedChallenge.getResult().getInfo().getComments()).isEqualTo(originalChallenge.getResult().getInfo().getComments());
        assertThat(convertedChallenge.getResult().getInfo().getAttachedImagePaths())
                .isEqualTo(originalChallenge.getResult().getInfo().getAttachedImagePaths());
    }

    @Test
    @DisplayName("JPA 엔티티에서 도메인 모델로 변환할 때 모든 필드가 정확하게 매핑된다")
    void givenEntity_whenConvertToDomain_thenAllFieldsAreMappedCorrectly() {
        // Given
        ChallengeJPAEntity challengeEntity = ChallengeJPAEntity.testInstance(
                1L,
                100L,
                "챌린저",
                new PeriodEmbeddable(LocalDate.now(), LocalDate.now().plusDays(7)),
                new GoalContentEmbeddable("목표 내용", "추가 내용", GoalType.POSITIVE),
                Arrays.asList("image1.png", "image2.png"),
                "certificate.png",
                new ChallengeResultEmbeddable(
                        ChallengeResultStatus.SUCCEEDED,
                        LocalDate.now().minusDays(1),
                        new AdditionalInfoEmbeddable("추가 정보 코멘트")
                ),
                Arrays.asList("additional_image1.png")
        );

        // When
        Challenge challenge = challengeEntity.toDomain();

        // Then
        assertThat(challenge).isNotNull();
        assertThat(challenge.getChallengeId()).isEqualTo(challengeEntity.getChallengeId());
        assertThat(challenge.getUserId()).isEqualTo(challengeEntity.getUserId());
        assertThat(challenge.getNickName()).isEqualTo(challengeEntity.getNickName());
        assertThat(challenge.getPeriod().startDate()).isEqualTo(challengeEntity.getPeriod().getStartDate());
        assertThat(challenge.getPeriod().endDate()).isEqualTo(challengeEntity.getPeriod().getEndDate());
        assertThat(challenge.getGoalContent().getMainContent()).isEqualTo(challengeEntity.getGoalContent().getMainContent());
        assertThat(challenge.getGoalContent().getAdditionalContent()).isEqualTo(challengeEntity.getGoalContent().getAdditionalContent());
        assertThat(challenge.getGoalContent().getType()).isEqualTo(challengeEntity.getGoalContent().getType());
        assertThat(challenge.getAttachedImagePaths()).isEqualTo(challengeEntity.getAttachedImagePaths());
        assertThat(challenge.getChallengeCertificateImagePath()).isEqualTo(challengeEntity.getChallengeCertificateImagePath());
        assertThat(challenge.getResult().getStatus()).isEqualTo(challengeEntity.getResult().getStatus());
        assertThat(challenge.getResult().getConfirmedDate()).isEqualTo(challengeEntity.getResult().getConfirmedDate());
        assertThat(challenge.getResult().getInfo().getComments()).isEqualTo(challengeEntity.getResult().getInfo().getComments());
        assertThat(challenge.getResult().getInfo().getAttachedImagePaths())
                .isEqualTo(challengeEntity.getAdditionalInfoAttachedImagePaths());
    }

    @Test
    @DisplayName("빈 컬렉션을 가진 도메인 모델을 변환할 때도 정상적으로 처리된다")
    void givenDomainModelWithEmptyCollections_whenConvertToEntityAndBack_thenHandledProperly() {
        // Given
        Challenge originalChallenge = Challenge.from(
                1L,
                100L,
                "챌린저",
                new Period(LocalDate.now(), LocalDate.now().plusDays(7)),
                GoalContent.from("목표 내용", "추가 내용", GoalType.POSITIVE),
                List.of(), // attachedImagePaths
                "certificate.png",
                ChallengeResult.from(
                        ChallengeResultStatus.SUCCEEDED,
                        LocalDate.now().minusDays(1),
                        AdditionalInfo.from(List.of(), "추가 정보 코멘트")
                )
        );

        // When
        ChallengeJPAEntity challengeEntity = ChallengeJPAEntity.fromDomain(originalChallenge);
        Challenge convertedChallenge = challengeEntity.toDomain();

        // Then
        assertThat(convertedChallenge).isNotNull();
        assertThat(convertedChallenge.getAttachedImagePaths()).isEmpty();
        assertThat(convertedChallenge.getResult().getInfo().getAttachedImagePaths()).isEmpty();
    }
}
