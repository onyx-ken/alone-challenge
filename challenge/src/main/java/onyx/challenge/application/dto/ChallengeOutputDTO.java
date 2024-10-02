package onyx.challenge.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import onyx.challenge.domain.model.Challenge;

@Data
@AllArgsConstructor
public class ChallengeOutputDTO {

    private Long challengeId;
    private int likeCount;

    public static ChallengeOutputDTO from(Challenge challenge, int likeCount) {
        return new ChallengeOutputDTO(challenge.getChallengeId(), likeCount);
    }

}
