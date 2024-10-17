package onyx.oauth.token.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onyx.oauth.error.TokenErrorResult;
import onyx.oauth.error.TokenException;
import onyx.oauth.jwt.JwtUtil;
import onyx.oauth.token.domain.RefreshToken;
import onyx.user.domain.entity.UserEntity;
import onyx.user.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserAuthenticationService userAuthenticationService;

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    public void issueAndSaveRefreshToken(UserEntity user) {
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), REFRESH_TOKEN_EXPIRATION_TIME);
        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .build();
        refreshTokenService.issueRefreshToken(newRefreshToken);
    }

    public void revokeAccessToken(UserEntity user) {
        refreshTokenService.deleteRefreshTokenByUserId(user.getId());
    }

    public String generateAccessToken(UserEntity user) {
        return jwtUtil.generateAccessToken(user.getId(), ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String refreshAccessToken(String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new TokenException(TokenErrorResult.INVALID_TOKEN);
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        log.info("Refreshing access token for userId: {}", userId);
        UserEntity user = userAuthenticationService.findUserByOauthProviderId(userId)
                .orElseThrow(() -> new TokenException(TokenErrorResult.INVALID_TOKEN));

        // 새로운 액세스 토큰 발급
        return jwtUtil.generateAccessToken(user.getId(), ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String refreshRefreshToken(String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new TokenException(TokenErrorResult.INVALID_TOKEN);
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        log.info("Refreshing refresh token for userId: {}", userId);
        // 기존 리프레시 토큰 무효화
        refreshTokenService.deleteRefreshTokenByUserId(Long.valueOf(userId));

        // 새로운 리프레시 토큰 발급
        return jwtUtil.generateRefreshToken(Long.valueOf(userId), REFRESH_TOKEN_EXPIRATION_TIME);
    }

}
