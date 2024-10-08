package onyx.challenge.application.service;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.like.LikeInputDTO;
import onyx.challenge.application.port.inbound.like.DeleteLikeUseCase;
import onyx.challenge.application.port.outbound.LikeRepository;
import onyx.challenge.domain.model.Like;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
@RequiredArgsConstructor
public class DeleteLikeService implements DeleteLikeUseCase {

    private final LikeRepository likeRepository;

    @Override
    public void deleteLike(LikeInputDTO likeInputDTO) {
        // 1. 도메인 모델 생성 또는 조회
        Like like = likeRepository.findByChallengeIdAndUserId(
                likeInputDTO.getChallengeId(),
                likeInputDTO.getUserId()
        ).orElseThrow(() -> new LikeNotFoundException("좋아요가 존재하지 않습니다."));

        likeRepository.deleteById(like.getLikeId());
    }
}
