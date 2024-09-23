package onyx.challenge.application.usecase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import onyx.challenge.domain.model.Challenge;

@Data
@AllArgsConstructor
public class ChallengeOutputDTO {

    private Long challengeId;

    public static ChallengeOutputDTO from(Challenge challenge) {
        return new ChallengeOutputDTO(challenge.getChallengeId());
    }

}
