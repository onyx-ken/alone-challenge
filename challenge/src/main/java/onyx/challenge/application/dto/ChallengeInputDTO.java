package onyx.challenge.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ChallengeInputDTO {
    private Long userId;
    private String nickName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String mainContent;
    private String additionalContent;
    private String goalType; // POSITIVE 또는 NEGATIVE
    private List<String> attachedImagePaths;
    private String challengeCertificateImagePath;
}
