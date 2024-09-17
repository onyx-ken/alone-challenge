package onyx.challenge.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AdditionalInfo {

    private final List<String> attachedImagePaths;

    private final String comments;

    public static AdditionalInfo from(List<String> attachedImagePaths, String comments) {
        return new AdditionalInfo(attachedImagePaths, comments);
    }

    public static AdditionalInfo createDefault() {
        return new AdditionalInfo();
    }

    public static AdditionalInfo create(List<String> attachedImagePaths, String comments) {
        return new AdditionalInfo(attachedImagePaths, comments);
    }

    private AdditionalInfo() {
        this.attachedImagePaths = new ArrayList<>();
        this.comments = "";
    }

    private AdditionalInfo(List<String> attachedImagePaths, String comments) {
        // 방어적 복사로 리스트의 불변성 유지
        this.attachedImagePaths = (attachedImagePaths != null) ? new ArrayList<>(attachedImagePaths) : new ArrayList<>();
        this.comments = (comments != null) ? comments : "";
    }

}
