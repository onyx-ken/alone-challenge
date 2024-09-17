package onyx.challenge.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.challenge.domain.GoalContent;
import onyx.challenge.domain.GoalType;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalContentEmbeddable {

    private String mainContent;
    private String additionalContent;

    @Enumerated(EnumType.STRING)
    private GoalType type;

    public GoalContentEmbeddable(String mainContent, String additionalContent, GoalType type) {
        this.mainContent = mainContent;
        this.additionalContent = additionalContent;
        this.type = type;
    }

    // 도메인 변환 메서드
    public GoalContent toDomain() {
        return GoalContent.from(this.mainContent, this.additionalContent, this.type);
    }

    public static GoalContentEmbeddable fromDomain(GoalContent goalContent) {
        return new GoalContentEmbeddable(
                goalContent.getMainContent(),
                goalContent.getAdditionalContent(),
                goalContent.getType()
        );
    }

}
