package onyx.challenge.application.port.outbound;

import onyx.challenge.domain.model.ChallengeImage;

import java.util.Optional;

public interface ChallengeImageRepository {
    ChallengeImage saveImage(ChallengeImage image);
    Optional<ChallengeImage> loadImage(Long imageId);
    void deleteImage(Long imageId);
}
