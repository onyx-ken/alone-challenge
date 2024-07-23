package onyx.user.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Profile {

    private String imageUrl;

    private String bio;

}
