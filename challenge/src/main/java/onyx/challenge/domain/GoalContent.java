package onyx.challenge.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public class GoalContent {

    private final String mainContent;
    private final String additionalContent;
    private final GoalType type;

    public static GoalContent from(String mainContent, String additionalContent, GoalType type) {
        return new GoalContent(mainContent, additionalContent, type);
    }

    public static GoalContent create(String mainContent, String additionalContent, GoalType type) {
        return new GoalContent(mainContent, additionalContent, type);
    }

    private GoalContent(String mainContent, String additionalContent, GoalType type) {
        this.mainContent = mainContent;
        this.additionalContent = additionalContent;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoalContent that = (GoalContent) o;
        return Objects.equals(mainContent, that.mainContent) && Objects.equals(additionalContent, that.additionalContent) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainContent, additionalContent, type);
    }
}
