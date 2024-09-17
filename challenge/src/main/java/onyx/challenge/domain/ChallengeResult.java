package onyx.challenge.domain;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ChallengeResult {

    private final ChallengeResultStatus status;
    private final LocalDate confirmedDate;
    private final AdditionalInfo info;

    public static ChallengeResult from(ChallengeResultStatus status, LocalDate confirmedDate, AdditionalInfo info) {
        return new ChallengeResult(status, confirmedDate, info);
    }

    public static ChallengeResult createDefault() {
        return new ChallengeResult(ChallengeResultStatus.ON_GOING, null, AdditionalInfo.createDefault());
    }

    public static ChallengeResult updateStatus(ChallengeResultStatus newStatus, LocalDate confirmedDate, AdditionalInfo newInfo) {
        if (newStatus == ChallengeResultStatus.ON_GOING) {
            throw new IllegalStateException("챌린지 결과 상태를 ON_GOING으로 변경할 수 없습니다.");
        }

        return new ChallengeResult(newStatus,
                confirmedDate == null ? LocalDate.now() : confirmedDate,
                newInfo == null ? AdditionalInfo.createDefault() : newInfo);
    }

    private ChallengeResult(ChallengeResultStatus status, LocalDate confirmedDate, AdditionalInfo info) {
        this.status = status;
        this.confirmedDate = confirmedDate;
        this.info = info;
    }

}
