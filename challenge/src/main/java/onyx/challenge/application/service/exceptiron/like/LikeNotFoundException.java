package onyx.challenge.application.service.exceptiron.like;

import onyx.challenge.application.service.exceptiron.common.NotFoundException;

public class LikeNotFoundException extends NotFoundException {
    public LikeNotFoundException(Long challengeId, Long userId) {
        super("like.not.found", challengeId, userId);
    }
}
