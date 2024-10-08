package onyx.challenge.application.dto.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LikeInputDTO {
    private final Long challengeId;
    private final Long userId;
}
