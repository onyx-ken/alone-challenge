package onyx.user.service;

import onyx.user.domain.entity.UserEntity;
import onyx.user.web.UserProfileUpdateRequest;

import java.io.IOException;
import java.util.Optional;

public interface UserInfoService {
    void updateUserInfo(Long userId, UserProfileUpdateRequest request) throws IOException;
    Optional<UserEntity> getUserInfo(Long userId);
}
