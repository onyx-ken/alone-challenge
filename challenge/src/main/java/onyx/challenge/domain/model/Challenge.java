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
    private final List<Long> attachedImageIds;
    private final ChallengeResult result;
    private final boolean isActive;

    public static Challenge from(Long challengeId, Long userId, String nickName, Period period,
                                 GoalContent goalContent, List<Long> attachedImageIds, ChallengeResult result, boolean isActive) {
        return new Challenge(challengeId, userId, nickName, period, goalContent, attachedImageIds, result, isActive);
    }

    public static Challenge create(Long challengeId, Long userId, String nickName, Period period, GoalContent goalContent, List<Long> attachedImageIds) {
        return new Challenge(challengeId, userId, nickName, period, goalContent, attachedImageIds, ChallengeResult.createDefault(), true);
    }

    public Challenge update(String nickName, Period period, GoalContent goalContent, List<Long> attachedImageIds,
                            ChallengeResult result, boolean isActive) {
        return new Challenge(
                this.challengeId,
                this.userId,
                nickName != null ? nickName : this.nickName,
                period != null ? period : this.period,
                goalContent != null ? goalContent : this.goalContent,
                attachedImageIds != null ? attachedImageIds : this.attachedImageIds,
                result != null ? result : this.result,
                isActive

        );
    }

    private Challenge(Long challengeId, Long userId, String nickName, Period period,
                      GoalContent goalContent, List<Long> attachedImageIds, ChallengeResult result, boolean isActive) {
        validateRequiredFields(userId, nickName, period, goalContent);
        this.challengeId = challengeId;
        this.userId = userId;
        this.nickName = nickName;
        this.period = period;
        this.goalContent = goalContent;
        this.attachedImageIds = attachedImageIds != null ? List.copyOf(attachedImageIds) : List.of();
        this.result = result != null ? result : ChallengeResult.createDefault();
        this.isActive = isActive;
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
