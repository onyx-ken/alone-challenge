package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.like.LikeInputDTO;
import onyx.challenge.application.dto.like.LikeOutputDTO;

public interface CreateLikeUseCase {
    LikeOutputDTO createLike(LikeInputDTO likeInputDTO);
}
