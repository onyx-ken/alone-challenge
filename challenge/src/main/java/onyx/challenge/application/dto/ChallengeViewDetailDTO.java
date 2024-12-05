package onyx.challenge.application.dto;

import lombok.Data;
import onyx.challenge.application.dto.comment.CommentOutputDTO;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChallengeViewDetailDTO {
    private final Long challengeId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<Long> imageIds;
    private final List<CommentOutputDTO> comments;
}
