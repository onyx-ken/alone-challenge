package onyx.challenge.application.service.like;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.like.LikeInputDTO;
import onyx.challenge.application.dto.like.LikeOutputDTO;
import onyx.challenge.application.port.inbound.like.CreateLikeUseCase;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.application.service.exceptiron.like.AlreadyLikedException;
import onyx.challenge.domain.model.Like;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateLikeService implements CreateLikeUseCase {

    private final LikeRepository likeRepository;

    @Override
    public LikeOutputDTO createLike(LikeInputDTO likeInputDTO) {
        Like like = Like.create(null, likeInputDTO.getChallengeId(), likeInputDTO.getUserId(), LocalDateTime.now());
        try {
            return LikeOutputDTO.from(likeRepository.save(like));
        }catch (DataIntegrityViolationException e) {
            // 유니크 제약 조건 위반 시 예외 처리
            throw new AlreadyLikedException(like.getChallengeId());
        }
    }
}
