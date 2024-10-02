package onyx.challenge.application.dto;

import lombok.Builder;
import lombok.Data;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.ChallengeResult;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.Period;

import java.util.List;

@Data
@Builder
public class ChallengeInquiryOutputDTO {

    private Long challengeId;
    private final Long userId;
    private final String nickName;
    private final Period period;
    private final GoalContent goalContent;
    private final List<Long> attachedImagePaths;
    private final ChallengeResult result;
    private final boolean isActive;
    private int likeCount;

    public static ChallengeInquiryOutputDTO from(Challenge challenge, int likeCount) {
        return ChallengeInquiryOutputDTO
                .builder()
                .challengeId(challenge.getChallengeId())
                .userId(challenge.getUserId())
                .nickName(challenge.getNickName())
                .period(challenge.getPeriod())
                .goalContent(challenge.getGoalContent())
                .attachedImagePaths(challenge.getAttachedImageIds())
                .result(challenge.getResult())
                .isActive(challenge.isActive())
                .likeCount(likeCount)
                .build();
    }

}
