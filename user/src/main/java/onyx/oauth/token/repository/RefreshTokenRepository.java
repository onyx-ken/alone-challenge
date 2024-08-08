package onyx.oauth.token.repository;

import onyx.oauth.token.domain.RefreshTokenRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenRedis, String> {
    Optional<RefreshTokenRedis> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
