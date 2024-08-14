package onyx.user.service;

import onyx.user.web.UserInfoUpdateRequest;

import java.io.IOException;

public interface UserInfoService {
    void updateUserInfo(Long userId, UserInfoUpdateRequest request) throws IOException;
}
