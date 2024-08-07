package onyx.oauth.token.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_tokens_id")
    private Long id;

    private Long userId;

    @Column(name = "token", nullable = false)
    private String token;

    public static RefreshTokenEntity fromDomain(RefreshToken token) {
        return RefreshTokenEntity.builder()
                .id(token.getId())
                .userId(token.getUserId())
                .token(token.getToken())
                .build();
    }

    public RefreshToken toDomain() {
        return RefreshToken.builder()
                .id(this.id)
                .userId(this.userId)
                .token(this.token)
                .build();
    }
}
