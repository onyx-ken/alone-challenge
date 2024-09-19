package onyx.challenge.framework.web;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;
import onyx.challenge.application.usecase.CreateChallengeUseCase;
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
    public ResponseEntity<ChallengeOutputDTO> createChallenge(@RequestBody ChallengeInputDTO request) {
        return ResponseEntity.ok(createChallengeUseCase.createChallenge(request));
    }

}
