package onyx.challenge.application.service.exceptiron.like;

import onyx.challenge.application.service.exceptiron.common.AlreadyExistsException;

public class AlreadyLikedException extends AlreadyExistsException {
    public AlreadyLikedException(Long challengeId) {
        super("like.already.exists", challengeId);
    }
}
