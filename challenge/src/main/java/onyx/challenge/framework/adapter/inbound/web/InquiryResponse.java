package onyx.challenge.framework.adapter.inbound.web;

import lombok.Data;
import onyx.challenge.application.dto.ChallengeViewDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
public class InquiryResponse {

    private final Long challengeId;
    private final String nickName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String mainContent;
    private final String additionalContent;
    private final String type;
    private final int likeCount;
    private final List<String> attachedImagesUrl;

    public static InquiryResponse create(ChallengeViewDTO dto, String imageBaseUrl) {
        return new InquiryResponse(
                dto.getChallengeId(), dto.getNickName(),
                dto.getStartDate(), dto.getEndDate(),
                dto.getMainContent(), dto.getAdditionalContent(),
                dto.getType(), dto.getLikeCount(),
                makeImagesUrl(dto.getAttachedImageIds(), imageBaseUrl));
    }

    private static List<String> makeImagesUrl(List<Long> attachedImageIds, String imageBaseUrl) {
        Objects.requireNonNull(imageBaseUrl, "imageBaseUrl must not be null");
        if (attachedImageIds == null || attachedImageIds.isEmpty()) {
            return Collections.emptyList();
        }

        return attachedImageIds.stream()
                .map(attachedImageId -> imageBaseUrl + attachedImageId)
                .toList();
    }
}
