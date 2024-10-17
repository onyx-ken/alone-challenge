package onyx.oauth.web;

import onyx.oauth.error.TokenException;
import onyx.oauth.token.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/auth")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String newAccessToken = tokenService.refreshAccessToken(request.getRefreshToken());
            String newRefreshToken = tokenService.refreshRefreshToken(request.getRefreshToken());

            // 새로운 토큰을 클라이언트에 반환
            return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
