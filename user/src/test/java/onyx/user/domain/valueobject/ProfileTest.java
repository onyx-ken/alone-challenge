package onyx.user.domain.valueobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTest {
    private Profile profile;

    @BeforeEach
    void setUp() {
        ProfileImage profileImage = ProfileImage.create("url1", "name1", 100, "jpg");
        profile = Profile.create(profileImage, "Initial bio");
    }

    @Test
    void updateProfile_shouldUpdateBioAndProfileImage() {
        // given
        ProfileImage newProfileImage = ProfileImage.create("newUrl", "newName", 200, "png");
        Profile newProfile = Profile.create(newProfileImage, "Updated bio");

        // when
        profile.updateProfile(newProfile);

        // then
        assertThat(profile.getBio()).isEqualTo("Updated bio");
        assertThat(profile.getProfileImage().getUrl()).isEqualTo("newUrl");
        assertThat(profile.getProfileImage().getName()).isEqualTo("newName");
        assertThat(profile.getProfileImage().getSize()).isEqualTo(200);
        assertThat(profile.getProfileImage().getFormat()).isEqualTo("png");
    }

}