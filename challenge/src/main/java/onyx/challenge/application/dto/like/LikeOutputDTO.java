package onyx.challenge.application.dto.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import onyx.challenge.domain.model.Like;

@Data
@AllArgsConstructor
public class LikeOutputDTO {
    private final Long challengeId;
    private final Long userId;
    private final boolean isLiked;

    public static LikeOutputDTO from(Like like) {
        return new LikeOutputDTO(like.getChallengeId(), like.getUserId(), true);
    }
}

