package onyx.challenge.framework.adapter.inbound.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateRequest {

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

    private List<ImageData> images = new ArrayList<>();

    public ChallengeInputDTO toDTO(Long userId) {
        return ChallengeInputDTO.builder()
                .userId(userId)
                .nickName(this.nickName)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .mainContent(this.mainContent)
                .additionalContent(this.additionalContent)
                .goalType(this.goalType)
                .attachedImages(images.stream()
                        .map(imageData -> new ChallengeInputDTO.ImageData(
                                convert(imageData.getFile()),
                                imageData.getOrder(),
                                imageData.getType()
                        ))
                        .toList())
                .build();
    }

    private FileData convert(MultipartFile multipartFile) {
        try {
            return FileData.create(
                    multipartFile.getOriginalFilename(),
                    multipartFile.getBytes(),
                    multipartFile.getContentType()
            );
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }
    }
}
