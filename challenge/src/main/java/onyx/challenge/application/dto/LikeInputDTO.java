package onyx.challenge.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeInputDTO {
    private final Long challengeId;
    private final Long userId;
}
