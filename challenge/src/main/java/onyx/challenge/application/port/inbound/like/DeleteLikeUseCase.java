package onyx.challenge.application.port.inbound.like;

import onyx.challenge.application.dto.like.LikeInputDTO;

public interface DeleteLikeUseCase {
    void deleteLike(LikeInputDTO likeInputDTO);
}
