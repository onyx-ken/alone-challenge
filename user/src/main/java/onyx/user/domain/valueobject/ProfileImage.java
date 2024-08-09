package onyx.user.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {

    private String url;
    private String name;
    private long size;
    private String format;

    public static ProfileImage create(String url, String name, long size, String format) {
        return new ProfileImage(url, name, size, format);
    }

    private ProfileImage(String url, String name, long size, String format) {
        this.url = url;
        this.name = name;
        this.size = size;
        this.format = format;
    }

    public static ProfileImage defaultImage() {
        return new ProfileImage("default-url", "default-image", 0, "default-format");
    }

}
