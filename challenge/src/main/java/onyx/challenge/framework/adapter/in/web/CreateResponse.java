package onyx.challenge.framework.adapter.in.web;

import lombok.Builder;
import lombok.Data;
import onyx.challenge.application.usecase.dto.ChallengeOutputDTO;

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
