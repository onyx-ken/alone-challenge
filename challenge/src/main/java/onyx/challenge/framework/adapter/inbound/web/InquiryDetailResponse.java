package onyx.challenge.framework.adapter.inbound.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import onyx.challenge.application.dto.ChallengeViewDetailDTO;
import onyx.challenge.framework.adapter.inbound.web.comment.CommentResponse;

import java.time.LocalDate;
import java.util.List;

@Data
public class InquiryDetailResponse {

    private final String challengeId; // 프론트엔드에서의 정밀도 문제로 String으로 반환해야 함.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate endDate;
    private final List<String> imageIds; // 프론트엔드에서의 정밀도 문제로 String으로 반환해야 함.
    private final List<CommentResponse> comments;

    public static InquiryDetailResponse create(ChallengeViewDetailDTO dto) {
        return new InquiryDetailResponse(
                dto.getChallengeId().toString(), dto.getStartDate(), dto.getEndDate(),
                dto.getImageIds().stream().map(Object::toString).toList(),
                dto.getComments().stream().map(CommentResponse::create).toList());
    }
}
