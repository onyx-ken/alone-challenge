package onyx.challenge.framework.adapter.inbound.web;

import lombok.Builder;
import lombok.Data;
import onyx.challenge.application.dto.ChallengeOutputDTO;

@Data
@Builder
public class CreateResponse {
    private Long challengeId;
    private String code;

    public static CreateResponse toDTO(ChallengeOutputDTO outputDTO) {
        return CreateResponse.builder()
                .challengeId(outputDTO.getChallengeId())
                .code("200")
                .build();
    }
}
