package onyx.challenge.framework.adapter.inbound.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.inbound.CreateChallengeUseCase;
import onyx.challenge.application.port.inbound.InquiryChallengeUseCase;
import onyx.challenge.framework.adapter.inbound.web.api.ApiResponse;
import onyx.challenge.framework.adapter.inbound.web.api.ApiStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final CreateChallengeUseCase createChallengeUseCase;
    private final InquiryChallengeUseCase inquiryChallengeUseCase;

    @Value("${image.base-url}")
    private String imageBaseUrl;

    @GetMapping("/welcome")
    String welcome() {
        return "Hello! Challenge";
    }

    @GetMapping("/challenges")
    public ResponseEntity<ApiResponse<List<InquiryResponse>>> getChallenges() {
        List<InquiryResponse> response = inquiryChallengeUseCase.getChallengeList()
                .stream().map(dto -> InquiryResponse.create(dto, imageBaseUrl)).toList();
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.SUCCESS, "챌린지목록을 정상적으로 불러왔습니다.", response));
    }

    @PostMapping("/challenges")
    public ResponseEntity<ApiResponse<CreateResponse>> createChallenge(@ModelAttribute @Valid CreateRequest request,
                                                                       @RequestHeader("X-User-Id") Long userId) {
        CreateResponse response = CreateResponse.toDTO(createChallengeUseCase.createChallenge(request.toDTO(userId)));
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.SUCCESS, "챌린지가 정상적으로 생성되었습니다.", response));
    }

}
