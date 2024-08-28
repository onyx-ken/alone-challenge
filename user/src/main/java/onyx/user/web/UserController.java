package onyx.user.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import onyx.oauth.jwt.JwtUtil;
import onyx.oauth.token.service.RefreshTokenService;
import onyx.user.domain.entity.UserEntity;
import onyx.user.domain.valueobject.*;
import onyx.user.service.UserAuthenticationService;
import onyx.user.service.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserAuthenticationService userAuthenticationService;

    private final RefreshTokenService refreshTokenService;

    private final UserInfoService userInfoService;

    private final JwtUtil jwtUtil;

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

    @PutMapping("/users/{userId}/profile")
    public ResponseEntity<Void> updateUserInfo(@PathVariable Long userId,
                                               @ModelAttribute UserProfileUpdateRequest request) {
        try {
            userInfoService.updateUserInfo(userId, request);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/token/{userId}")
    public ResponseEntity<String> getUsersTokenInfo(@PathVariable Long userId) {

        return refreshTokenService.findRefreshTokenByUserId(userId)
                .map(refreshTokenRedis -> new ResponseEntity<>(refreshTokenRedis.getToken(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("Token not found", HttpStatus.NOT_FOUND));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable Long userId) {

        return userInfoService.getUserInfo(userId)
                .map(UserInfoResponse::from)  // UserEntity -> UserInfoResponse 변환
                .map(ResponseEntity::ok)  // UserInfoResponse -> ResponseEntity로 변환
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // 없을 경우 404 반환
    }

    @GetMapping("/users/me/profile")
    public ResponseEntity<UserInfoResponse> getCurrentUserInfo(HttpServletRequest request) {

        return userInfoService.getUserInfo(Long.valueOf(jwtUtil.getUserIdFromHeader(request)))
                .map(UserInfoResponse::from)  // UserEntity -> UserInfoResponse 변환
                .map(ResponseEntity::ok)  // UserInfoResponse -> ResponseEntity로 변환
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)); // 없을 경우 404 반환
    }

    @PutMapping("/users/me/profile")
    public ResponseEntity<Void> updateUserProfile(HttpServletRequest request,
                                                  @ModelAttribute UserProfileUpdateRequest userProfileUpdateRequest) {
        try {
            // 사용자 정보 업데이트
            userInfoService.updateUserInfo(Long.valueOf(jwtUtil.getUserIdFromHeader(request)), userProfileUpdateRequest);

            // 업데이트 성공 응답
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // 모든 예외처리, 추후 사용자 찾지 못하는 경우, 권한 없는 경우 등 에러 추가 처리 필요.
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
