package onyx.challenge.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import onyx.challenge.domain.model.Challenge;

@Data
@AllArgsConstructor
public class ChallengeOutputDTO {
    private Long challengeId;
    private String message;

    public static ChallengeOutputDTO from(Challenge challenge) {
        return new ChallengeOutputDTO(challenge.getChallengeId(), "Challenge를 생성했습니다.");
    }
}
