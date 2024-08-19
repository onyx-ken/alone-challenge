package onyx.user.domain.entity;

import onyx.user.domain.valueobject.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class UserEntityTest {

    @Test
    @DisplayName("처음 사용자를 생성 한다.")
    void createUser() {

        // GIVEN

        String nickName = "운영자";
        final Email email = Email.createFromFullAddress("dnshewhsy@google.com");
        final OauthInfo oauthInfo = OauthInfo.create(Provider.GOOGLE, "qwjsndwj23w");

        // WHEN
        final UserEntity userEntity = UserEntity.create(nickName, email, oauthInfo);

        // THEN
        assertThat(userEntity.getNickName()).isEqualTo(nickName);
        assertThat(userEntity.getEmail().getEmailAddress()).isEqualTo("dnshewhsy@google.com");
        assertThat(userEntity.oauthInfo.getProvider()).isEqualTo(Provider.GOOGLE);
        assertThat(userEntity.oauthInfo.getProviderId()).isEqualTo("qwjsndwj23w");
        assertThat(userEntity.getPoints()).isZero();

        // 기본 프로필 값 검증
        assertThat(userEntity.getProfile()).isNotNull();
        assertThat(userEntity.getProfile().getBio()).isEqualTo("Default bio");

        // 기본 프로필 이미지 값 검증
        assertThat(userEntity.getProfile().getProfileImage()).isNotNull();

    }

    @Test
    @DisplayName("프로필 이미지를 수정한다.")
    void updateProfile_shouldUpdateUserProfile() {
        // given
        String nickName = "운영자";
        final Email email = Email.createFromFullAddress("dnshewhsy@google.com");
        final OauthInfo oauthInfo = OauthInfo.create(Provider.GOOGLE, "qwjsndwj23w");
        final UserEntity userEntity = UserEntity.create(nickName, email, oauthInfo);

        ProfileImage newProfileImage = ProfileImage.create("newUrl", "newName", 200, "png");
        Profile newProfile = Profile.create(newProfileImage, "Updated bio");

        // when
        userEntity.updateProfile(newProfile);

        // then
        assertThat(userEntity.getProfile().getBio()).isEqualTo("Updated bio");

    }

}