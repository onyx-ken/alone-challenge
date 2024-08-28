package onyx.user.domain.valueobject;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.file.domain.FileInfo;

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
        if (profile.getProfileImage() != null) {
            updateProfileImage(profile.getProfileImage());
        }
        if (profile.getBio() != null && !profile.getBio().isEmpty()) {
            updateBio(profile.getBio());
        }
    }

    public void updateBio(String bio) {
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
