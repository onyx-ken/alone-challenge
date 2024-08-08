package onyx.user.service;

import onyx.user.domain.entity.UserEntity;

import java.util.Optional;

public interface UserService {
    UserEntity register(UserEntity user);
    Optional<UserEntity> findUserByOauthProviderId(String providerId);
}
