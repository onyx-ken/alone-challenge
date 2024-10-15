package onyx.challenge.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import onyx.challenge.domain.vo.ImageType;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ChallengeModifyDTO {
    private Long challengeId;
    private Long userId;
    private String nickName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String mainContent;
    private String additionalContent;
    private String goalType; // POSITIVE 또는 NEGATIVE
    private List<ImageData> attachedImages;

    @Data
    @AllArgsConstructor
    public static class ImageData {
        private Long imageId;
        private FileData fileData;
        private int order;
        private ImageType type;
    }
}
