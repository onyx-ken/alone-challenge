package onyx.challenge.application.port.inbound.like;

import java.util.List;

public interface InquiryLikedChallengeIdUseCase {
    List<Long> getLikedChallengeIds(List<Long> challengeIds, Long userId);
}
