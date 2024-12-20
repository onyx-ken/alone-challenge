package onyx.challenge.framework.adapter.outbound.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.AdditionalInfo;
import onyx.challenge.domain.vo.ChallengeResult;
import org.hibernate.type.YesNoConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "challenge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChallengeJPAEntity {

    @Id @Tsid
    private Long challengeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String nickName;

    @Embedded
    private PeriodEmbeddable period;

    @Embedded
    private GoalContentEmbeddable goalContent;

    @ElementCollection
    @CollectionTable(name = "challenge_attached_images", joinColumns = @JoinColumn(name = "challenge_id"))
    @Column(name = "attached_image_id")
    private List<Long> attachedImages = new ArrayList<>();

    @Embedded
    private ChallengeResultEmbeddable result;

    @ElementCollection
    @CollectionTable(name = "additional_info_images", joinColumns = @JoinColumn(name = "challenge_id"))
    @Column(name = "image_path")
    private List<String> additionalInfoAttachedImagePaths;

    @Convert(converter = YesNoConverter.class)
    private boolean isActive = true;

    // 도메인 모델로부터 변환
    public static ChallengeJPAEntity fromDomain(Challenge challenge) {
        ChallengeJPAEntity entity = new ChallengeJPAEntity();
        entity.challengeId = challenge.getChallengeId();
        entity.userId = challenge.getUserId();
        entity.nickName = challenge.getNickName();
        entity.period = PeriodEmbeddable.fromDomain(challenge.getPeriod());
        entity.goalContent = GoalContentEmbeddable.fromDomain(challenge.getGoalContent());
        entity.attachedImages = challenge.getAttachedImageIds();
        entity.result = ChallengeResultEmbeddable.fromDomain(challenge.getResult());
        entity.additionalInfoAttachedImagePaths = challenge.getResult().getInfo().getAttachedImagePaths() != null
                ? List.copyOf(challenge.getResult().getInfo().getAttachedImagePaths())
                : List.of();
        entity.isActive = challenge.isActive();
        return entity;
    }

    public Challenge toDomain() {
        AdditionalInfo info = (this.result != null && this.result.getInfo() != null)
                ? this.result.getInfo().toDomain(this.additionalInfoAttachedImagePaths)
                : AdditionalInfo.from(this.additionalInfoAttachedImagePaths, null);
        ChallengeResult result = ChallengeResult.from(
                this.result.getStatus(),
                this.result.getConfirmedDate(),
                info
        );
        return Challenge.from(
                this.challengeId,
                this.userId,
                this.nickName,
                this.period.toDomain(),
                this.goalContent.toDomain(),
                this.attachedImages,
                result,
                this.isActive
        );
    }

    static ChallengeJPAEntity testInstance(
            Long challengeId,
            Long userId,
            String nickName,
            PeriodEmbeddable period,
            GoalContentEmbeddable goalContent,
            List<Long> attachedImagePaths,
            ChallengeResultEmbeddable result,
            List<String> additionalInfoAttachedImagePaths
    ) {
        ChallengeJPAEntity entity = new ChallengeJPAEntity();
        entity.challengeId = challengeId;
        entity.userId = userId;
        entity.nickName = nickName;
        entity.period = period;
        entity.goalContent = goalContent;
        entity.attachedImages = attachedImagePaths;
        entity.result = result;
        entity.additionalInfoAttachedImagePaths = additionalInfoAttachedImagePaths;
        entity.isActive = true;
        return entity;
    }
}
