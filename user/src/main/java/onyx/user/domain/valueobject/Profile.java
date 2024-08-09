package onyx.user.domain.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Embedded
    private ProfileImage profileImage;

    private String bio;

    public static Profile defaultProfile() {
        return new Profile(ProfileImage.defaultImage(), "Default bio");
    }

    public static Profile create(ProfileImage profileImage, String bio) {
        return new Profile(profileImage, bio);
    }

    public void updateProfile(Profile profile) {
        updateProfileImage(profile.getProfileImage());
        updateBio(profile.getBio());
    }

    private void updateBio(String bio) {
        this.bio = bio;
    }

    private void updateProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    private Profile(ProfileImage profileImage, String bio) {
        this.profileImage = profileImage;
        this.bio = bio;
    }

}
