package onyx.oauth.token.repository;

import onyx.oauth.token.domain.RefreshToken;
import onyx.oauth.token.domain.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    void save(RefreshToken refreshToken);
    void deleteByUserId(Long userId);
}
