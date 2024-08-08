package onyx.oauth.token.service;

import lombok.RequiredArgsConstructor;
import onyx.oauth.jwt.JwtUtil;
import onyx.oauth.token.domain.RefreshToken;
import onyx.user.domain.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

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

}
