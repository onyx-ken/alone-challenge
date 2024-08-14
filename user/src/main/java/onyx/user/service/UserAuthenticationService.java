package onyx.user.service;

import onyx.user.domain.entity.UserEntity;

import java.util.Optional;

public interface UserAuthenticationService {
    UserEntity register(UserEntity user);
    Optional<UserEntity> findUserByOauthProviderId(String providerId);
}
