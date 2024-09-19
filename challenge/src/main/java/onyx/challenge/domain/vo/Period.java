package onyx.challenge.domain.vo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record Period(LocalDate startDate, LocalDate endDate) {

    public Period {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("종료일은 시작일 이후여야 합니다.");
        }
    }

    public DurationCategory getDurationCategory() {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days < 7) {
            return DurationCategory.SHORT_TERM;
        } else if (days <= 14) {
            return DurationCategory.MEDIUM_TERM;
        } else if (days <= 31) {
            return DurationCategory.LONG_TERM;
        } else {
            throw new IllegalStateException("유효하지 않은 기간입니다.");
        }
    }

}
