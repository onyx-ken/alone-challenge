package onyx.oauth.token.service;

import lombok.RequiredArgsConstructor;
import onyx.oauth.token.domain.RefreshToken;
import onyx.oauth.token.domain.RefreshTokenEntity;
import onyx.oauth.token.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Override
    public void issueRefreshToken(RefreshToken token) {
        refreshTokenRepository.save(RefreshTokenEntity.fromDomain(token));
    }

    @Transactional
    @Override
    public void deleteRefreshTokenByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
