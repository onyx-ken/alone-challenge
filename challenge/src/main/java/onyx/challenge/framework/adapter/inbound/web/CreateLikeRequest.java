package onyx.challenge.framework.adapter.inbound.web;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateLikeRequest {
    @NotNull(message = "userId는 필수입니다.")
    private Long userId;
}
