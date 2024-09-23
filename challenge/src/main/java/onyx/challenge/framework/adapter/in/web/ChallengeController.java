package onyx.challenge.framework.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onyx.challenge.application.usecase.CreateChallengeUseCase;
import onyx.challenge.framework.adapter.in.web.api.ApiResponse;
import onyx.challenge.framework.adapter.in.web.api.ApiStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final CreateChallengeUseCase createChallengeUseCase;

    @GetMapping("/welcome")
    String welcome() {
        return "Hello! Challenge";
    }

    @PostMapping("/challenges")
    public ResponseEntity<ApiResponse<CreateResponse>> createChallenge(@RequestBody @Valid CreateRequest request) {
        CreateResponse response = CreateResponse.toDTO(createChallengeUseCase.createChallenge(request.toDTO()));
        return ResponseEntity.ok(ApiResponse.create(ApiStatus.SUCCESS, "챌린지가 정상적으로 생성되었습니다.", response));
    }

}
