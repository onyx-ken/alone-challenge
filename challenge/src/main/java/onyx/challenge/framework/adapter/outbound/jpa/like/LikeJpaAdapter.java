package onyx.challenge.framework.adapter.outbound.jpa.like;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.application.service.exceptiron.like.AlreadyLikedException;
import onyx.challenge.domain.model.Like;
import onyx.challenge.framework.adapter.outbound.jpa.entity.LikeJPAEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeJpaAdapter implements LikeRepository {

    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Like save(Like like) throws AlreadyLikedException { // unchecked 예외지만 명시적으로 선언
        try {
            return likeJpaRepository.save(LikeJPAEntity.fromDomain(like)).toDomain();
        } catch (DataIntegrityViolationException e) { // 유니크 제약 조건 위반시 발생
            throw new AlreadyLikedException(like.getChallengeId());
        }
    }

    @Override
    public Optional<Like> load(Long likeId) {
        return likeJpaRepository.findById(likeId).map(LikeJPAEntity::toDomain);
    }

    @Override
    public int countByChallengeId(Long challengeId) {
        return likeJpaRepository.countByChallengeId(challengeId);
    }

    @Override
    public void deleteById(Long likeId) {
        likeJpaRepository.deleteById(likeId);
    }

    @Override
    public Optional<Like> findByChallengeIdAndUserId(Long challengeId, Long userId) {
        return likeJpaRepository.findByChallengeIdAndUserId(challengeId, userId)
                .map(LikeJPAEntity::toDomain);
    }
}
