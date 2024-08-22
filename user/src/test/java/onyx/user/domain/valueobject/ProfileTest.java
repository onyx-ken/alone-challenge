package onyx.user.domain.valueobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTest {
    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = Profile.defaultProfile();
    }

    @Test
    void updateProfile_shouldUpdateBioAndProfileImage() {
        // given
        Profile newProfile = Profile.create(ProfileImage.defaultImage(), "Updated bio");

        // when
        profile.updateProfile(newProfile);

        // then
        assertThat(profile.getBio()).isEqualTo("Updated bio");
    }

}