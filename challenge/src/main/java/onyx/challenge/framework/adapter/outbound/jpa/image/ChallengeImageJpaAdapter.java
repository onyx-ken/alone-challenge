package onyx.challenge.framework.adapter.outbound.jpa.image;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.outbound.ChallengeImageRepository;
import onyx.challenge.domain.model.ChallengeImage;
import onyx.challenge.framework.adapter.outbound.jpa.entity.ChallengeImageJPAEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChallengeImageJpaAdapter implements ChallengeImageRepository {

    private final ChallengeImageJpaRepository challengeImageJpaRepository;

    @Override
    public ChallengeImage saveImage(ChallengeImage image) {
        ChallengeImageJPAEntity entity = ChallengeImageJPAEntity.fromDomain(image);
        return challengeImageJpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<ChallengeImage> loadImage(Long imageId) {
        return challengeImageJpaRepository.findById(imageId).map(ChallengeImageJPAEntity::toDomain);
    }
}
