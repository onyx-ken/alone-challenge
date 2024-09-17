package onyx.challenge;

import onyx.challenge.domain.DurationCategory;
import onyx.challenge.domain.Period;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PeriodTest {

    @Test
    @DisplayName("종료일이 시작일 이전이면 IllegalArgumentException이 발생한다")
    void whenEndDateIsBeforeStartDate_thenThrowsException() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 10, 10);
        LocalDate endDate = LocalDate.of(2024, 10, 9);

        // When & Then
        Assertions.assertThatThrownBy(() -> new Period(startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("종료일은 시작일 이후여야 합니다.");
    }

    @Test
    @DisplayName("기간이 7일 미만이면 SHORT_TERM을 반환한다")
    void whenPeriodIsLessThan7Days_thenReturnsShortTerm() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 5); // 4일 간격

        // When
        Period period = new Period(startDate, endDate);
        DurationCategory category = period.getDurationCategory();

        // Then
        Assertions.assertThat(category).isEqualTo(DurationCategory.SHORT_TERM);
    }

    @Test
    @DisplayName("기간이 7일부터 14일까지면 MEDIUM_TERM을 반환한다")
    void whenPeriodIsBetween7And14Days_thenReturnsMediumTerm() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 8); // 7일 간격

        // When
        Period period = new Period(startDate, endDate);
        DurationCategory category = period.getDurationCategory();

        // Then
        Assertions.assertThat(category).isEqualTo(DurationCategory.MEDIUM_TERM);
    }

    @Test
    @DisplayName("기간이 15일부터 31일까지면 LONG_TERM을 반환한다")
    void whenPeriodIsBetween15And31Days_thenReturnsLongTerm() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 20); // 19일 간격

        // When
        Period period = new Period(startDate, endDate);
        DurationCategory category = period.getDurationCategory();

        // Then
        Assertions.assertThat(category).isEqualTo(DurationCategory.LONG_TERM);
    }

    @Test
    @DisplayName("기간이 31일을 초과하면 IllegalStateException이 발생한다")
    void whenPeriodExceeds31Days_thenThrowsException() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 5); // 35일 간격

        // When & Then
        Period period = new Period(startDate, endDate);
        Assertions.assertThatThrownBy(period::getDurationCategory)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("유효하지 않은 기간입니다.");
    }

    @Test
    @DisplayName("시작일과 종료일이 같으면 SHORT_TERM을 반환한다")
    void whenStartDateEqualsEndDate_thenReturnsShortTerm() {
        // Given
        LocalDate date = LocalDate.of(2024, 10, 1);

        // When
        Period period = new Period(date, date);
        DurationCategory category = period.getDurationCategory();

        // Then
        Assertions.assertThat(category).isEqualTo(DurationCategory.SHORT_TERM);
    }

    @Test
    @DisplayName("기간이 정확히 14일이면 MEDIUM_TERM을 반환한다")
    void whenPeriodIsExactly14Days_thenReturnsMediumTerm() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 15); // 14일 간격

        // When
        Period period = new Period(startDate, endDate);
        DurationCategory category = period.getDurationCategory();

        // Then
        Assertions.assertThat(category).isEqualTo(DurationCategory.MEDIUM_TERM);
    }

    @Test
    @DisplayName("기간이 정확히 31일이면 LONG_TERM을 반환한다")
    void whenPeriodIsExactly31Days_thenReturnsLongTerm() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 1); // 31일 간격

        // When
        Period period = new Period(startDate, endDate);
        DurationCategory category = period.getDurationCategory();

        // Then
        Assertions.assertThat(category).isEqualTo(DurationCategory.LONG_TERM);
    }

    @Test
    @DisplayName("기간이 음수일 경우 IllegalArgumentException이 발생한다")
    void whenPeriodIsNegative_thenThrowsException() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 10, 5);
        LocalDate endDate = LocalDate.of(2024, 10, 1); // 종료일이 시작일보다 이전

        // When & Then
        Assertions.assertThatThrownBy(() -> new Period(startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("종료일은 시작일 이후여야 합니다.");
    }

}
