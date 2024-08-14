package onyx.user.web;

import lombok.RequiredArgsConstructor;
import onyx.oauth.token.domain.RefreshTokenRedis;
import onyx.oauth.token.service.RefreshTokenService;
import onyx.user.domain.entity.UserEntity;
import onyx.user.domain.valueobject.*;
import onyx.user.service.UserAuthenticationService;
import onyx.user.service.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserAuthenticationService userAuthenticationService;

    private final RefreshTokenService refreshTokenService;

    private final UserInfoService userInfoService;

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
        final UserEntity registeredUser = userAuthenticationService.register(newUser);

        // 등록된 사용자의 정보를 반환
        return new ResponseEntity<>(registeredUser.getNickName(), HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}/info")
    public ResponseEntity<Void> updateUserInfo(@PathVariable Long userId,
                                               @ModelAttribute UserInfoUpdateRequest request) {
        try {
            userInfoService.updateUserInfo(userId, request);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/token/{userId}")
    public ResponseEntity<String> getUsersTokenInfo(@PathVariable Long userId) {
        Optional<RefreshTokenRedis> refreshTokenByUserId = refreshTokenService.findRefreshTokenByUserId(userId);

        return refreshTokenByUserId
                .map(refreshTokenRedis -> new ResponseEntity<>(refreshTokenRedis.getToken(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("Token not found", HttpStatus.NOT_FOUND));
    }

}
