package onyx.challenge.domain.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChallengeCreatedEvent implements DomainEvent {

    private Long challengeId;
    private Long userId;
    private LocalDateTime occurredOn;

    public static ChallengeCreatedEvent create(Long challengeId, Long userId) {
        return new ChallengeCreatedEvent(challengeId, userId);
    }

    private ChallengeCreatedEvent(Long challengeId, Long userId) {
        this.challengeId = challengeId;
        this.userId = userId;
        this.occurredOn = LocalDateTime.now();
    }

    @Override
    public LocalDateTime occurredOn() {
        return occurredOn;
    }
}
