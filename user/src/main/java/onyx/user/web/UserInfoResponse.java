package onyx.user.web;

import lombok.Data;
import onyx.user.domain.entity.UserEntity;
import onyx.user.domain.valueobject.Provider;

@Data
public class UserInfoResponse {
    private String nickName;
    private String email;
    private String bio;
    private long profileImageId;
    private int points;
    private Provider provider;

    public static UserInfoResponse from(UserEntity user) {
        UserInfoResponse response = new UserInfoResponse();
        response.setNickName(user.getNickName());
        response.setEmail(user.getEmail().getEmailAddress());
        response.setBio(user.getProfile().getBio());
        response.setPoints(user.getPoints());
        response.setProvider(user.getOauthInfo().getProvider());

        // ProfileImage가 null이 아닌 경우에만 profileImageId를 설정
        if (user.getProfile().getProfileImage() != null &&
                user.getProfile().getProfileImage().getFileInfo() != null) {
            response.setProfileImageId(user.getProfile().getProfileImage().getFileInfo().getId());
        } else {
            response.setProfileImageId(0); // 기본값을 설정 (0)
        }

        return response;
    }
}

