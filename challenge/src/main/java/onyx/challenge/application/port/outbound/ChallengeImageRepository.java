package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.ChallengeImage;

public interface ChallengeImageRepository {
    ChallengeImage saveImage(ChallengeImage image);
    ChallengeImage loadImage(Long imageId);
}
