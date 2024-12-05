package onyx.challenge.framework.adapter.inbound.web.like;

import lombok.Data;

import java.util.List;

@Data
public class InquiryLikedChallengeIdRequest {
    private List<String> challengeIds;
}
