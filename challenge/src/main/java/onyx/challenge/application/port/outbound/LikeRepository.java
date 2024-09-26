package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.Like;

public interface LikeRepository {
    Like save(Like like);
}
