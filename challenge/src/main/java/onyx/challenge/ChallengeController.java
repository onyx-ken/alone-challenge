package onyx.challenge;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChallengeController {
    @GetMapping("/welcome")
    String welcome() {
        return "Hello! Challenge";
    }
}
