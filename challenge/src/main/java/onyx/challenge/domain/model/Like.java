package onyx.challenge.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

// todo 추후 댓글 좋아요 기능이 생기면 별도의 인터페이스로 만들어서, 분리 관리 할 것!
@Getter
public class Like {

    private final Long likeId;
    private final Long challengeId;
    private final Long userId;
    private final LocalDateTime likedAt;

    public static Like create(Long likeId, Long challengeId, Long userId, LocalDateTime likedAt) {
        if (challengeId == null || userId == null) {
            throw new IllegalArgumentException("challengeId와 userId는 반드시 포함되어야 합니다.");
        }
        if (likedAt == null) {
            likedAt = LocalDateTime.now();
        }
        return new Like(likeId, challengeId, userId, likedAt);
    }

    public static Like from(Long likeId, Long challengeId, Long userId, LocalDateTime likedAt) {
        return new Like(likeId, challengeId, userId, likedAt);
    }

    private Like(Long likeId, Long challengeId, Long userId, LocalDateTime likedAt) {
        this.likeId = likeId;
        this.challengeId = challengeId;
        this.userId = userId;
        this.likedAt = likedAt;
    }

}
