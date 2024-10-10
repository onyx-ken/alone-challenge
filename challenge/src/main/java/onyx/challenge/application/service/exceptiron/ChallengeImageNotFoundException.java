package onyx.challenge.application.service.exceptiron;

import onyx.challenge.application.service.exceptiron.common.NotFoundException;

public class ChallengeImageNotFoundException extends NotFoundException {
    public ChallengeImageNotFoundException(Long imageId) {
        super("challenge.image.not.found", imageId);
    }
}
