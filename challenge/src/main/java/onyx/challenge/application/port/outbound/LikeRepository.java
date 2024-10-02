package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.Like;

import java.util.List;

public interface LikeRepository {
    Like save(Like like);
    List<Like> load(Long ChallengeId);
    int countByChallengeId(Long challengeId);
}
