package onyx.oauth.token.service;

import onyx.oauth.token.domain.RefreshToken;
import onyx.oauth.token.domain.RefreshTokenRedis;

import java.util.Optional;

public interface RefreshTokenService {
    void issueRefreshToken(RefreshToken token);
    void deleteRefreshTokenByUserId(Long userId);
    Optional<RefreshTokenRedis> findRefreshTokenByUserId(Long userId);
}
