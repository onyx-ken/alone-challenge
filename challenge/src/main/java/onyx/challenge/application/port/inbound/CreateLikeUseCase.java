package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.LikeInputDTO;
import onyx.challenge.application.dto.LikeOutputDTO;

public interface CreateLikeUseCase {
    LikeOutputDTO createLike(LikeInputDTO likeInputDTO);
}
