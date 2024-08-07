package onyx.user.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    private String imageUrl;

    private String bio;

    public static Profile create(String imageUrl, String bio) {
        return new Profile(imageUrl, bio);
    }

    public void updateBio(String bio) {
        this.bio = bio;
    }

    private Profile(String imageUrl, String bio) {
        this.imageUrl = imageUrl;
        this.bio = bio;
    }

}
