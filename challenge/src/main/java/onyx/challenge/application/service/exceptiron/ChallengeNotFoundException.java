package onyx.challenge.application.service.exceptiron;

import onyx.challenge.application.service.exceptiron.common.NotFoundException;

public class ChallengeNotFoundException extends NotFoundException {
    public ChallengeNotFoundException(Long commentId) {
        super("challenge.not.found", commentId);
    }
}
