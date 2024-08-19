package onyx.user.domain.valueobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTest {
    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = Profile.create(profileImage, "Initial bio");
    }

    @Test
    void updateProfile_shouldUpdateBioAndProfileImage() {
        // given
        Profile newProfile = Profile.create(newProfileImage, "Updated bio");

        // when
        profile.updateProfile(newProfile);

        // then
        assertThat(profile.getBio()).isEqualTo("Updated bio");
    }

}