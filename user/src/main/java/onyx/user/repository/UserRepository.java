package onyx.user.repository;

import onyx.user.domain.entity.UserEntity;
import onyx.user.domain.valueobject.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(Email email);
    Optional<UserEntity> findByOauthInfoProviderId(String providerId);
}
