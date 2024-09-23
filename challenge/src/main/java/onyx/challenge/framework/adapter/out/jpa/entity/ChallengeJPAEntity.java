package onyx.challenge.framework.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.challenge.domain.vo.AdditionalInfo;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.ChallengeResult;

import java.util.List;

@Entity
@Table(name = "challenge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChallengeJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "image_path")
    private List<String> attachedImagePaths;

    private String challengeCertificateImagePath;

    @Embedded
    private ChallengeResultEmbeddable result;

    @ElementCollection
    @CollectionTable(name = "additional_info_images", joinColumns = @JoinColumn(name = "challenge_id"))
    @Column(name = "image_path")
    private List<String> additionalInfoAttachedImagePaths;

    // 도메인 모델로부터 변환
    public static ChallengeJPAEntity fromDomain(Challenge challenge) {
        ChallengeJPAEntity entity = new ChallengeJPAEntity();
        entity.challengeId = challenge.getChallengeId();
        entity.userId = challenge.getUserId();
        entity.nickName = challenge.getNickName();
        entity.period = PeriodEmbeddable.fromDomain(challenge.getPeriod());
        entity.goalContent = GoalContentEmbeddable.fromDomain(challenge.getGoalContent());
        entity.attachedImagePaths = challenge.getAttachedImagePaths();
        entity.challengeCertificateImagePath = challenge.getChallengeCertificateImagePath();
        entity.result = ChallengeResultEmbeddable.fromDomain(challenge.getResult());
        entity.additionalInfoAttachedImagePaths = challenge.getResult().getInfo().getAttachedImagePaths() != null
                ? List.copyOf(challenge.getResult().getInfo().getAttachedImagePaths())
                : List.of();
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
                this.attachedImagePaths,
                this.challengeCertificateImagePath,
                result
        );
    }

    static ChallengeJPAEntity testInstance(
            Long challengeId,
            Long userId,
            String nickName,
            PeriodEmbeddable period,
            GoalContentEmbeddable goalContent,
            List<String> attachedImagePaths,
            String challengeCertificateImagePath,
            ChallengeResultEmbeddable result,
            List<String> additionalInfoAttachedImagePaths
    ) {
        ChallengeJPAEntity entity = new ChallengeJPAEntity();
        entity.challengeId = challengeId;
        entity.userId = userId;
        entity.nickName = nickName;
        entity.period = period;
        entity.goalContent = goalContent;
        entity.attachedImagePaths = attachedImagePaths;
        entity.challengeCertificateImagePath = challengeCertificateImagePath;
        entity.result = result;
        entity.additionalInfoAttachedImagePaths = additionalInfoAttachedImagePaths;
        return entity;
    }
}
