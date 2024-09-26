package onyx.challenge.framework.adapter.outbound.jpa.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.challenge.domain.model.Like;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "challenge_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LikeJPAEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "challenge_id", nullable = false)
    private Long challengeId;

    private LocalDateTime likedAt;

    public static LikeJPAEntity fromDomain(Like like) {
        LikeJPAEntity entity = new LikeJPAEntity();
        entity.likeId = like.getLikeId();
        entity.challengeId = like.getChallengeId();
        entity.userId = like.getUserId();
        return entity;
    }

    public Like toDomain() {
        return Like.from(this.likeId, this.challengeId, this.userId, this.likedAt);
    }

    static LikeJPAEntity testInstance(Long likeId, Long challengeId, Long userId, LocalDateTime likedAt) {
        LikeJPAEntity entity = new LikeJPAEntity();
        entity.likeId = likeId;
        entity.challengeId = challengeId;
        entity.userId = userId;
        entity.likedAt = likedAt;
        return entity;
    }

}
