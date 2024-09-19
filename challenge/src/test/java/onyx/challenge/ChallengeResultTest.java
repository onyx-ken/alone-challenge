package onyx.challenge;

import onyx.challenge.domain.vo.AdditionalInfo;
import onyx.challenge.domain.vo.ChallengeResult;
import onyx.challenge.domain.vo.ChallengeResultStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class ChallengeResultTest {

    @Test
    @DisplayName("ChallengeResult를 생성하면 기본 값이 설정된다")
    void whenCreatingNewChallengeResult_thenDefaultsAreSet() {
        // Given & When
        ChallengeResult defaultChallengeResult = ChallengeResult.createDefault();

        // Then
        Assertions.assertThat(defaultChallengeResult.getStatus()).isEqualTo(ChallengeResultStatus.ON_GOING);
        Assertions.assertThat(defaultChallengeResult.getConfirmedDate()).isNull();
        Assertions.assertThat(defaultChallengeResult.getInfo().getAttachedImagePaths()).isEmpty();
        Assertions.assertThat(defaultChallengeResult.getInfo().getComments()).isEmpty();
    }

    @Test
    @DisplayName("ChallengeResult를 변경하면, 전달한 값 그대로 변경이 된다.")
    void whenUpdatingStatusToSucceeded_thenStatusAndDateAreUpdated() {
        // Given
        ChallengeResult initialResult = ChallengeResult.createDefault();
        AdditionalInfo newInfo = AdditionalInfo.create(List.of("image1.png"), "챌린지 성공!");
        LocalDate confirmedDate = LocalDate.of(2024, 9, 22);

        // When
        ChallengeResult updatedResult = ChallengeResult.updateStatus(
                ChallengeResultStatus.SUCCEEDED,
                confirmedDate,
                newInfo
        );

        // Then
        Assertions.assertThat(updatedResult.getStatus()).isEqualTo(ChallengeResultStatus.SUCCEEDED);
        Assertions.assertThat(updatedResult.getConfirmedDate()).isEqualTo(confirmedDate);
        Assertions.assertThat(updatedResult.getInfo()).isEqualTo(newInfo);
    }

    @Test
    @DisplayName("상태를 ON_GOING으로 변경하려고 하면 예외가 발생한다")
    void whenUpdatingStatusToOnGoing_thenExceptionIsThrown() {
        // Given
        ChallengeResult initialResult = ChallengeResult.createDefault();
        AdditionalInfo newInfo = AdditionalInfo.create(List.of("image1.png"), "챌린지 성공!");
        LocalDate confirmedDate = LocalDate.of(2024, 9, 22);
        // When & Then
        Assertions.assertThatThrownBy(() -> ChallengeResult.updateStatus(
                        ChallengeResultStatus.ON_GOING,
                        confirmedDate,
                        newInfo
                )).isInstanceOf(IllegalStateException.class)
                .hasMessage("챌린지 결과 상태를 ON_GOING으로 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("confirmedDate가 null이면 현재 날짜로 설정된다")
    void whenConfirmedDateIsNull_thenSetToCurrentDate() {
        // Given
        AdditionalInfo info = AdditionalInfo.createDefault();

        // When
        ChallengeResult updatedResult = ChallengeResult.updateStatus(
                ChallengeResultStatus.FAILED,
                null,
                info
        );

        // Then
        Assertions.assertThat(updatedResult.getConfirmedDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("새로운 info가 null이면 기본 info로 설정된다")
    void whenNewInfoIsNull_thenSetToDefaultInfo() {
        // Given
        ChallengeResult initialResult = ChallengeResult.createDefault();

        // When
        ChallengeResult updatedResult = ChallengeResult.updateStatus(
                ChallengeResultStatus.FAILED,
                null,
                null
        );

        // Then
        Assertions.assertThat(updatedResult.getInfo()).isNotNull();
        Assertions.assertThat(updatedResult.getInfo().getAttachedImagePaths()).isEmpty();
        Assertions.assertThat(updatedResult.getInfo().getComments()).isEmpty();
    }

}
