package onyx.challenge.framework.adapter.outbound.jpa.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.challenge.domain.vo.AdditionalInfo;
import onyx.challenge.domain.vo.ChallengeResult;
import onyx.challenge.domain.vo.ChallengeResultStatus;

import java.time.LocalDate;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeResultEmbeddable {

    @Enumerated(EnumType.STRING)
    private ChallengeResultStatus status;

    private LocalDate confirmedDate;

    @Embedded
    private AdditionalInfoEmbeddable info;

    public ChallengeResultEmbeddable(ChallengeResultStatus status, LocalDate confirmedDate, AdditionalInfoEmbeddable info) {
        this.status = status;
        this.confirmedDate = confirmedDate;
        this.info = info;
    }

    // attachedImagePaths 파라미터 추가
    public ChallengeResult toDomain(List<String> attachedImagePaths) {
        AdditionalInfo additionalInfo = this.info.toDomain(attachedImagePaths);
        return ChallengeResult.from(this.status, this.confirmedDate, additionalInfo);
    }

    public static ChallengeResultEmbeddable fromDomain(ChallengeResult result) {
        return new ChallengeResultEmbeddable(
                result.getStatus(),
                result.getConfirmedDate(),
                AdditionalInfoEmbeddable.fromDomain(result.getInfo())
        );
    }
}