package onyx.challenge.application.port.outbound;

import onyx.challenge.application.service.exceptiron.like.AlreadyLikedException;
import onyx.challenge.domain.model.Like;

import java.util.Optional;

public interface LikeRepository {
    Like save(Like like) throws AlreadyLikedException; // unchecked 예외지만 명시적으로 선언
    Optional<Like> load(Long likeId);
    int countByChallengeId(Long challengeId);
    void deleteById(Long likeId);
    Optional<Like> findByChallengeIdAndUserId(Long challengeId, Long userId);
}
