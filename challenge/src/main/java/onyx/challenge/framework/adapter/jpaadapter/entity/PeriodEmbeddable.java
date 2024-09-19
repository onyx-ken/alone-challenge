package onyx.challenge.framework.adapter.jpaadapter.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.challenge.domain.vo.Period;

import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeriodEmbeddable {

    private LocalDate startDate;
    private LocalDate endDate;

    public PeriodEmbeddable(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // 도메인 변환 메서드
    public Period toDomain() {
        return new Period(this.startDate, this.endDate);
    }

    public static PeriodEmbeddable fromDomain(Period period) {
        return new PeriodEmbeddable(period.startDate(), period.endDate());
    }

}
