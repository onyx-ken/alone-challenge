package onyx.challenge.domain.model;

import lombok.Getter;
import onyx.challenge.domain.vo.ChallengeResult;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.Period;

import java.util.List;

@Getter
public class Challenge {

    private final Long challengeId;
    private final Long userId;
    private final String nickName;
    private final Period period;
    private final GoalContent goalContent;
    private final List<String> attachedImagePaths;
    private final String challengeCertificateImagePath;
    private final ChallengeResult result;

    public static Challenge from(Long challengeId, Long userId, String nickName, Period period,
                                 GoalContent goalContent, List<String> attachedImagePaths,
                                 String challengeCertificateImagePath, ChallengeResult result) {
        return new Challenge(challengeId, userId, nickName, period, goalContent, attachedImagePaths, challengeCertificateImagePath, result);
    }

    public static Challenge create(Long challengeId, Long userId, String nickName, Period period,
                                   GoalContent goalContent, List<String> attachedImagePaths,
                                   String challengeCertificateImagePath) {
        return new Challenge(challengeId, userId, nickName, period, goalContent, attachedImagePaths,
                challengeCertificateImagePath, ChallengeResult.createDefault());
    }

    public Challenge update(String nickName, Period period, GoalContent goalContent,
                            List<String> attachedImagePaths, String challengeCertificateImagePath, ChallengeResult result) {
        return new Challenge(
                this.challengeId,
                this.userId,
                nickName != null ? nickName : this.nickName,
                period != null ? period : this.period,
                goalContent != null ? goalContent : this.goalContent,
                attachedImagePaths != null ? attachedImagePaths : this.attachedImagePaths,
                challengeCertificateImagePath != null ? challengeCertificateImagePath : this.challengeCertificateImagePath,
                result != null ? result : this.result
        );
    }

    private Challenge(Long challengeId, Long userId, String nickName, Period period, GoalContent goalContent,
                      List<String> attachedImagePaths, String challengeCertificateImagePath, ChallengeResult result) {
        validateRequiredFields(userId, nickName, period, goalContent);
        this.challengeId = challengeId;
        this.userId = userId;
        this.nickName = nickName;
        this.period = period;
        this.goalContent = goalContent;
        // 방어적 복사를 통해 리스트의 불변성 유지
        this.attachedImagePaths = (attachedImagePaths != null) ? List.copyOf(attachedImagePaths) : List.of();
        this.challengeCertificateImagePath = challengeCertificateImagePath;
        this.result = result != null ? result : ChallengeResult.createDefault();
    }

    private void validateRequiredFields(Long userId, String nickName,
                                        Period period, GoalContent goalContent) {
        if (userId == null) {
            throw new IllegalArgumentException("회원 ID는 필수입니다.");
        }
        if (nickName == null) {
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }
        if (period == null) {
            throw new IllegalArgumentException("도전 기간은 필수입니다.");
        }
        if (goalContent == null) {
            throw new IllegalArgumentException("도전 내용은 필수입니다.");
        }
    }

}
