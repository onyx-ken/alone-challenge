package onyx.challenge.framework.adapter.outbound.jpa;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.domain.model.Like;
import onyx.challenge.framework.adapter.outbound.jpa.entity.LikeJPAEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeJpaAdapter implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Like save(Like like) {
        return likeJpaRepository.save(LikeJPAEntity.fromDomain(like)).toDomain();
    }

}
