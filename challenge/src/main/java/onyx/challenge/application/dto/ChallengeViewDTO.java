package onyx.challenge.application.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChallengeViewDTO {
    private final Long challengeId;
    private final String nickName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String mainContent;
    private final String additionalContent;
    private final String type;
    private final int likeCount;
    private final List<Long> attachedImageIds;

}
