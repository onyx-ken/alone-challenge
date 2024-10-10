package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.Like;

import java.util.Optional;

public interface LikeRepository {
    Like save(Like like);
    Optional<Like> load(Long likeId);
    int countByChallengeId(Long challengeId);
    void deleteById(Long likeId);
    Optional<Like> findByChallengeIdAndUserId(Long challengeId, Long userId);
}
