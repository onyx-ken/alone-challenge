package onyx.challenge.application.service.like;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.inbound.like.InquiryLikedChallengeIdUseCase;
import onyx.challenge.application.port.outbound.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryLikedChallengeIdService implements InquiryLikedChallengeIdUseCase {

    private final LikeRepository likeRepository;

    @Override
    public List<Long> getLikedChallengeIds(List<Long> challengeIds, Long userId) {

        return likeRepository.findByUserIdAndChallengeIdIn(userId, challengeIds);
    }

}
