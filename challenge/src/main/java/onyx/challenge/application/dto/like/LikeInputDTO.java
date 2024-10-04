package onyx.challenge.application.dto.like;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeInputDTO {
    private final Long challengeId;
    private final Long userId;
}
