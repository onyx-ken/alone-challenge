package onyx.oauth.token.service;

import lombok.RequiredArgsConstructor;
import onyx.oauth.token.domain.RefreshToken;
import onyx.oauth.token.domain.RefreshTokenRedis;
import onyx.oauth.token.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Override
    public void issueRefreshToken(RefreshToken token) {
        refreshTokenRepository.save(RefreshTokenRedis.fromDomain(token));
    }

    @Transactional
    @Override
    public void deleteRefreshTokenByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    public Optional<RefreshTokenRedis> findRefreshTokenByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }
}
