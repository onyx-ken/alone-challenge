package onyx.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/welcome")
    String welcome() {
        return "Hello! User";
    }
}
