package onyx.challenge.framework.adapter.outbound.jpa;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.outbound.ChallengeImageRepository;
import onyx.challenge.application.service.StorageFileNotFoundException;
import onyx.challenge.domain.model.ChallengeImage;
import onyx.challenge.framework.adapter.outbound.jpa.entity.ChallengeImageJPAEntity;
import org.springframework.stereotype.Repository;

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
    public ChallengeImage loadImage(Long imageId) {
        ChallengeImageJPAEntity entity = challengeImageJpaRepository.findById(imageId)
                .orElseThrow(() -> new StorageFileNotFoundException("이미지를 찾을 수 없습니다: " + imageId));
        return entity.toDomain();
    }
}
