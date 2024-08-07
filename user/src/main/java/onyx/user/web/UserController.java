package onyx.user.web;

import lombok.RequiredArgsConstructor;
import onyx.user.domain.entity.UserEntity;
import onyx.user.domain.valueobject.Email;
import onyx.user.domain.valueobject.OauthInfo;
import onyx.user.domain.valueobject.Provider;
import onyx.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * for Test in SpringCloud or SimpleAPI Call
     * @return hardCoded Text
     */

    @GetMapping("/welcome")
    String welcome() {
        return "Hello! User";
    }

    @PostMapping("/users")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
        // 요청 데이터로부터 User 도메인 객체 생성
        UserEntity newUser = UserEntity.create(
                request.getNickName(),
                Email.createFromFullAddress(request.getEmailAddress()),
                OauthInfo.create(Provider.valueOf(request.getProvider()), request.getProviderId()));

        // User 도메인 객체를 엔티티 객체로 변환하여 사용자 등록
        final UserEntity registeredUser = userService.register(newUser);

        // 등록된 사용자의 정보를 반환
        return new ResponseEntity<>(registeredUser.getNickName(), HttpStatus.CREATED);
    }

}
