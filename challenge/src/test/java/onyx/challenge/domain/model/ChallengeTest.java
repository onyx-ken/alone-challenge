package onyx.challenge.domain.model;

import onyx.challenge.domain.vo.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
        List<Long> attachedImageIds = Arrays.asList(10L, 11L);

        // When
        Challenge challenge = Challenge.create(challengeId, userId, nickName, period, goalContent, attachedImageIds);

        // Then
        Assertions.assertThat(challenge.getChallengeId()).isEqualTo(challengeId);
        Assertions.assertThat(challenge.getUserId()).isEqualTo(userId);
        Assertions.assertThat(challenge.getNickName()).isEqualTo(nickName);
        Assertions.assertThat(challenge.getPeriod()).isEqualTo(period);
        Assertions.assertThat(challenge.getGoalContent()).isEqualTo(goalContent);
        Assertions.assertThat(challenge.getAttachedImageIds()).containsExactlyElementsOf(attachedImageIds);
        Assertions.assertThat(challenge.getResult()).isNotNull();
        Assertions.assertThat(challenge.getResult().getStatus()).isEqualTo(ChallengeResultStatus.ON_GOING);
        Assertions.assertThat(challenge.isActive()).isTrue();
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
        List<Long> attachedImageIds = null;

        // When & Then
        Assertions.assertThatThrownBy(() -> Challenge.create(challengeId, userId, nickName, period, goalContent, attachedImageIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("회원 ID는 필수입니다.");
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
        List<Long> attachedImageIds = Arrays.asList(10L, 11L);

        Challenge challenge = Challenge.create(challengeId, userId, nickName, period, goalContent, attachedImageIds);

        // 업데이트할 필드
        String newNickName = "업데이트된 챌린저";
        Period newPeriod = new Period(LocalDate.now(), LocalDate.now().plusDays(14));
        GoalContent newGoalContent = GoalContent.create("새로운 목표", "새로운 추가 내용", GoalType.NEGATIVE);
        List<Long> newAttachedImageIds = Arrays.asList(20L, 21L);
        ChallengeResult newResult = ChallengeResult.updateStatus(ChallengeResultStatus.SUCCEEDED, null, null);
        boolean newIsActive = false;

        // When
        Challenge updatedChallenge = challenge.update(newNickName, newPeriod, newGoalContent, newAttachedImageIds, newResult, newIsActive);

        // Then
        Assertions.assertThat(updatedChallenge).isNotSameAs(challenge);
        Assertions.assertThat(updatedChallenge.getNickName()).isEqualTo(newNickName);
        Assertions.assertThat(updatedChallenge.getPeriod()).isEqualTo(newPeriod);
        Assertions.assertThat(updatedChallenge.getGoalContent()).isEqualTo(newGoalContent);
        Assertions.assertThat(updatedChallenge.getAttachedImageIds()).isEqualTo(newAttachedImageIds);
        Assertions.assertThat(updatedChallenge.getResult()).isEqualTo(newResult);
        Assertions.assertThat(updatedChallenge.isActive()).isFalse();

        // 원본 객체는 변경되지 않음
        Assertions.assertThat(challenge.getNickName()).isEqualTo(nickName);
        Assertions.assertThat(challenge.getPeriod()).isEqualTo(period);
        Assertions.assertThat(challenge.getGoalContent()).isEqualTo(goalContent);
        Assertions.assertThat(challenge.getAttachedImageIds()).isEqualTo(attachedImageIds);
        Assertions.assertThat(challenge.getResult()).isNotEqualTo(newResult);
        Assertions.assertThat(challenge.isActive()).isNotEqualTo(newResult);
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
        List<Long> attachedImageIds = Arrays.asList(10L, 11L);

        // When & Then
        Assertions.assertThatThrownBy(() -> Challenge.create(challengeId, userId, nickName, period, goalContent, attachedImageIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("회원 ID는 필수입니다.");
    }

    @Test
    @DisplayName("update 메서드로 isActive 필드를 업데이트하면 새로운 객체에 반영된다")
    void whenUpdatingIsActive_thenNewObjectHasUpdatedIsActive() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String nickName = "챌린저";
        Period period = new Period(LocalDate.now(), LocalDate.now().plusDays(7));
        GoalContent goalContent = GoalContent.create("목표 내용", "추가 내용", GoalType.POSITIVE);
        List<Long> attachedImageIds = Arrays.asList(10L, 11L);

        Challenge challenge = Challenge.create(challengeId, userId, nickName, period, goalContent, attachedImageIds);

        // When
        Challenge updatedChallenge = challenge.update(null, null, null, null, null, false);

        // Then
        Assertions.assertThat(updatedChallenge.isActive()).isFalse();
    }

    @Test
    @DisplayName("ChallengeResult가 null이면 기본값으로 설정된다")
    void whenChallengeResultIsNull_thenSetToDefault() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String nickName = "챌린저";
        Period period = new Period(LocalDate.now(), LocalDate.now().plusDays(7));
        GoalContent goalContent = GoalContent.create("목표 내용", "추가 내용", GoalType.POSITIVE);
        List<Long> attachedImageIds = Arrays.asList(10L, 11L);
        ChallengeResult result = null;

        // When
        Challenge challenge = Challenge.from(challengeId, userId, nickName, period, goalContent, attachedImageIds, result, true);

        // Then
        Assertions.assertThat(challenge.getResult()).isNotNull();
        Assertions.assertThat(challenge.getResult().getStatus()).isEqualTo(ChallengeResultStatus.ON_GOING);
    }

}