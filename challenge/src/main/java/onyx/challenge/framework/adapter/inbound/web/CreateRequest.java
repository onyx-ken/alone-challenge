package onyx.challenge.framework.adapter.inbound.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.FileInputData;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateRequest {

    @NotNull(message = "userId는 필수입니다.")
    private Long userId;

    @NotBlank(message = "nickName은 필수입니다.")
    private String nickName;

    @NotNull(message = "startDate는 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "endDate는 필수입니다.")
    private LocalDate endDate;

    @NotBlank(message = "mainContent는 필수입니다.")
    private String mainContent;

    private String additionalContent;

    @NotBlank(message = "goalType은 필수입니다.")
    private String goalType; // POSITIVE 또는 NEGATIVE

    private List<MultipartFile> attachedImages = new ArrayList<>();

    public ChallengeInputDTO toDTO() {
        return ChallengeInputDTO.builder()
                .userId(this.userId)
                .nickName(this.nickName)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .mainContent(this.mainContent)
                .additionalContent(this.additionalContent)
                .goalType(this.goalType)
                .attachedImages(attachedImages.stream().map(FileInputData::convert).toList())
                .build();
    }
}
