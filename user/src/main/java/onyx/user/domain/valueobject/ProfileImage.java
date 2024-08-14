package onyx.user.domain.valueobject;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.file.domain.FileInfo;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_info_id")
    private FileInfo fileInfo;

    public static ProfileImage defaultImage() {
        return new ProfileImage();
    }

    public static ProfileImage create(FileInfo fileInfo) {
        return new ProfileImage(fileInfo);
    }

    private ProfileImage(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
