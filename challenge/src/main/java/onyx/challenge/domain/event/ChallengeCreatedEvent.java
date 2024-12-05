package onyx.challenge.domain.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChallengeCreatedEvent implements DomainEvent {

    private Long challengeId;
    private Long userId;
    private String userNickName;
    private String startDt;
    private String endDt;
    private String target;
    private String userAddBody;
    private LocalDateTime occurredOn;

    public static ChallengeCreatedEvent create(Long challengeId, Long userId, String userNickName,
                                               String startDt, String endDt, String target,
                                               String userAddBody, LocalDateTime occurredOn) {
        return new ChallengeCreatedEvent(challengeId, userId, userNickName, startDt, endDt, target, userAddBody, occurredOn);
    }

    private ChallengeCreatedEvent(Long challengeId, Long userId, String userNickName,
                                  String startDt, String endDt,
                                  String target, String userAddBody,
                                  LocalDateTime occurredOn) {
        this.challengeId = challengeId;
        this.userId = userId;
        this.userNickName = userNickName;
        this.startDt = startDt;
        this.endDt = endDt;
        this.target = target;
        this.userAddBody = userAddBody;
        this.occurredOn = occurredOn;
    }

    @Override
    public LocalDateTime occurredOn() {
        return occurredOn;
    }
}
