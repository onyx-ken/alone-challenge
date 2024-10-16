package onyx.challenge.framework.adapter.inbound.web;

import lombok.Data;
import onyx.challenge.application.dto.ChallengeViewDTO;

import java.time.LocalDate;
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
    private final int commentCount;
    private final String firstAttachedImageUrl;

    public static InquiryResponse create(ChallengeViewDTO dto, String imageBaseUrl) {
        return new InquiryResponse(
                dto.getChallengeId(), dto.getNickName(),
                dto.getStartDate(), dto.getEndDate(),
                dto.getMainContent(), dto.getAdditionalContent(),
                dto.getType(), dto.getLikeCount(),dto.getCommentCount(),
                makeImagesUrl(dto.getFirstImageId(), imageBaseUrl));
    }

    private static String makeImagesUrl(Long attachedImageIds, String imageBaseUrl) {
        Objects.requireNonNull(imageBaseUrl, "imageBaseUrl must not be null");
        if (attachedImageIds == null) {
            throw new IllegalArgumentException("attachedImageIds must not be null");
        }
        return imageBaseUrl + attachedImageIds;
    }
}
