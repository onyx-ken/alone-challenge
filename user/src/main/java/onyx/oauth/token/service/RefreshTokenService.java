package onyx.oauth.token.service;

import onyx.oauth.token.domain.RefreshToken;

public interface RefreshTokenService {
    void issueRefreshToken(RefreshToken token);
    void deleteRefreshTokenByUserId(Long userId);
}
