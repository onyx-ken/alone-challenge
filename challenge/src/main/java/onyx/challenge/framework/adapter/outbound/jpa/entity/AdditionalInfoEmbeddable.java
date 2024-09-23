package onyx.challenge.framework.adapter.outbound.jpa.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.challenge.domain.vo.AdditionalInfo;

import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalInfoEmbeddable {

    private String comments;

    public AdditionalInfoEmbeddable(String comments) {
        this.comments = comments;
    }

    // 도메인 변환 메서드
    public AdditionalInfo toDomain(List<String> attachedImagePaths) {
        return AdditionalInfo.from(attachedImagePaths, this.comments);
    }

    public static AdditionalInfoEmbeddable fromDomain(AdditionalInfo info) {
        return new AdditionalInfoEmbeddable(info.getComments());
    }

}
