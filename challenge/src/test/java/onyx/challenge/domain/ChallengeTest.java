package onyx.challenge.domain;

import onyx.challenge.domain.model.*;
import onyx.challenge.domain.vo.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ChallengeTest {

    @Test
    @DisplayName("유효한 데이터로 Challenge를 생성하면 성공한다")
    void whenCreatingChallengeWithValidData_thenSuccess() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String nickName = "챌린저";
        Period period = new Period(LocalDate.now(), LocalDate.now().plusDays(7));
        GoalContent goalContent = GoalContent.create("목표 내용", "추가 내용", GoalType.POSITIVE);
        List<String> attachedImagePaths = Arrays.asList("image1.png", "image2.png");
        String challengeCertificateImagePath = "certificate.png";

        // When
        Challenge challenge = Challenge.create(challengeId, userId, nickName, period, goalContent,
                attachedImagePaths, challengeCertificateImagePath);

        // Then
        Assertions.assertThat(challenge.getChallengeId()).isEqualTo(challengeId);
        Assertions.assertThat(challenge.getUserId()).isEqualTo(userId);
        Assertions.assertThat(challenge.getNickName()).isEqualTo(nickName);
        Assertions.assertThat(challenge.getPeriod()).isEqualTo(period);
        Assertions.assertThat(challenge.getGoalContent()).isEqualTo(goalContent);
        Assertions.assertThat(challenge.getAttachedImagePaths()).containsExactlyElementsOf(attachedImagePaths);
        Assertions.assertThat(challenge.getChallengeCertificateImagePath()).isEqualTo(challengeCertificateImagePath);
        Assertions.assertThat(challenge.getResult()).isNotNull();
        Assertions.assertThat(challenge.getResult().getStatus()).isEqualTo(ChallengeResultStatus.ON_GOING);
    }

    @Test
    @DisplayName("필수 필드가 null이면 IllegalArgumentException이 발생한다")
    void whenRequiredFieldsAreNull_thenThrowsException() {
        // Given
        Long challengeId = null;
        Long userId = null;
        String nickName = null;
        Period period = null;
        GoalContent goalContent = null;
        List<String> attachedImagePaths = null;
        String challengeCertificateImagePath = null;

        // When & Then
        Assertions.assertThatThrownBy(() -> Challenge.create(challengeId, userId, nickName, period, goalContent,
                        attachedImagePaths, challengeCertificateImagePath))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("챌린지 ID는 필수입니다.");
    }

    @Test
    @DisplayName("update 메서드로 필드를 업데이트하면 새로운 객체가 생성되고 필드가 변경된다")
    void whenUpdatingFields_thenNewObjectIsCreatedWithUpdatedFields() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String nickName = "챌린저";
        Period period = new Period(LocalDate.now(), LocalDate.now().plusDays(7));
        GoalContent goalContent = GoalContent.create("목표 내용", "추가 내용", GoalType.POSITIVE);
        List<String> attachedImagePaths = Arrays.asList("image1.png", "image2.png");
        String challengeCertificateImagePath = "certificate.png";

        Challenge challenge = Challenge.create(challengeId, userId, nickName, period, goalContent,
                attachedImagePaths, challengeCertificateImagePath);

        // 업데이트할 필드
        String newNickName = "업데이트된 챌린저";
        Period newPeriod = new Period(LocalDate.now(), LocalDate.now().plusDays(14));
        GoalContent newGoalContent = GoalContent.create("새로운 목표", "새로운 추가 내용", GoalType.NEGATIVE);
        List<String> newAttachedImagePaths = Arrays.asList("new_image1.png");
        String newChallengeCertificateImagePath = "new_certificate.png";
        ChallengeResult newResult = ChallengeResult.updateStatus(ChallengeResultStatus.SUCCEEDED, null, null);

        // When
        Challenge updatedChallenge = challenge.update(newNickName, newPeriod, newGoalContent,
                newAttachedImagePaths, newChallengeCertificateImagePath, newResult);

        // Then
        Assertions.assertThat(updatedChallenge).isNotSameAs(challenge);
        Assertions.assertThat(updatedChallenge.getNickName()).isEqualTo(newNickName);
        Assertions.assertThat(updatedChallenge.getPeriod()).isEqualTo(newPeriod);
        Assertions.assertThat(updatedChallenge.getGoalContent()).isEqualTo(newGoalContent);
        Assertions.assertThat(updatedChallenge.getAttachedImagePaths()).containsExactlyElementsOf(newAttachedImagePaths);
        Assertions.assertThat(updatedChallenge.getChallengeCertificateImagePath()).isEqualTo(newChallengeCertificateImagePath);
        Assertions.assertThat(updatedChallenge.getResult()).isEqualTo(newResult);

        // 원본 객체는 변경되지 않음
        Assertions.assertThat(challenge.getNickName()).isEqualTo(nickName);
        Assertions.assertThat(challenge.getPeriod()).isEqualTo(period);
        Assertions.assertThat(challenge.getGoalContent()).isEqualTo(goalContent);
        Assertions.assertThat(challenge.getAttachedImagePaths()).containsExactlyElementsOf(attachedImagePaths);
        Assertions.assertThat(challenge.getChallengeCertificateImagePath()).isEqualTo(challengeCertificateImagePath);
        Assertions.assertThat(challenge.getResult()).isNotEqualTo(newResult);
    }

    @Test
    @DisplayName("update 메서드로 null 값을 전달하면 기존 값이 유지된다")
    void whenUpdatingWithNullValues_thenFieldsRemainUnchanged() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String nickName = "챌린저";
        Period period = new Period(LocalDate.now(), LocalDate.now().plusDays(7));
        GoalContent goalContent = GoalContent.create("목표 내용", "추가 내용", GoalType.POSITIVE);
        List<String> attachedImagePaths = Arrays.asList("image1.png", "image2.png");
        String challengeCertificateImagePath = "certificate.png";
        ChallengeResult result = ChallengeResult.createDefault();

        Challenge challenge = Challenge.create(challengeId, userId, nickName, period, goalContent,
                attachedImagePaths, challengeCertificateImagePath);

        // When
        Challenge updatedChallenge = challenge.update(null, null, null, null, null, null);

        // Then
        Assertions.assertThat(updatedChallenge).isNotSameAs(challenge);
        Assertions.assertThat(updatedChallenge.getNickName()).isEqualTo(challenge.getNickName());
        Assertions.assertThat(updatedChallenge.getPeriod()).isEqualTo(challenge.getPeriod());
        Assertions.assertThat(updatedChallenge.getGoalContent()).isEqualTo(challenge.getGoalContent());
        Assertions.assertThat(updatedChallenge.getAttachedImagePaths()).isEqualTo(challenge.getAttachedImagePaths());
        Assertions.assertThat(updatedChallenge.getChallengeCertificateImagePath()).isEqualTo(challenge.getChallengeCertificateImagePath());
        Assertions.assertThat(updatedChallenge.getResult()).isEqualTo(challenge.getResult());
    }

    @Test
    @DisplayName("attachedImagePaths가 null이면 빈 리스트로 초기화된다")
    void whenAttachedImagePathsIsNull_thenInitializeWithEmptyList() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String nickName = "챌린저";
        Period period = new Period(LocalDate.now(), LocalDate.now().plusDays(7));
        GoalContent goalContent = GoalContent.create("목표 내용", "추가 내용", GoalType.POSITIVE);
        List<String> attachedImagePaths = null;
        String challengeCertificateImagePath = "certificate.png";

        // When
        Challenge challenge = Challenge.create(challengeId, userId, nickName, period, goalContent,
                attachedImagePaths, challengeCertificateImagePath);

        // Then
        Assertions.assertThat(challenge.getAttachedImagePaths()).isEmpty();
    }

    @Test
    @DisplayName("방어적 복사를 통해 리스트의 불변성을 유지한다")
    void whenModifyingOriginalList_thenChallengeListRemainsUnchanged() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String nickName = "챌린저";
        Period period = new Period(LocalDate.now(), LocalDate.now().plusDays(7));
        GoalContent goalContent = GoalContent.create("목표 내용", "추가 내용", GoalType.POSITIVE);
        List<String> attachedImagePaths = new ArrayList<>(Arrays.asList("image1.png", "image2.png"));
        String challengeCertificateImagePath = "certificate.png";

        Challenge challenge = Challenge.create(challengeId, userId, nickName, period, goalContent,
                attachedImagePaths, challengeCertificateImagePath);

        // 원본 리스트를 수정
        attachedImagePaths.add("image3.png");

        // When & Then
        Assertions.assertThat(challenge.getAttachedImagePaths()).doesNotContain("image3.png");
        Assertions.assertThat(challenge.getAttachedImagePaths()).containsExactly("image1.png", "image2.png");
    }

    @Test
    @DisplayName("필수 필드가 null이면 생성자에서 예외가 발생한다")
    void whenCreatingWithNullRequiredFields_thenThrowsException() {
        // Given
        Long challengeId = 1L;
        Long userId = null; // 필수 필드를 null로 설정
        String nickName = "챌린저";
        Period period = null; // 필수 필드를 null로 설정
        GoalContent goalContent = GoalContent.create("목표 내용", "추가 내용", GoalType.POSITIVE);
        List<String> attachedImagePaths = null;
        String challengeCertificateImagePath = null;

        // When & Then
        Assertions.assertThatThrownBy(() -> Challenge.create(challengeId, userId, nickName, period, goalContent,
                        attachedImagePaths, challengeCertificateImagePath))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("회원 ID는 필수입니다.");
    }

}