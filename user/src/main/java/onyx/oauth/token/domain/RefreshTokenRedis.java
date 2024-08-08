package onyx.oauth.token.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRedis implements Serializable {
    @Id
    private Long id;

    @Indexed
    private Long userId;

    private String token;

    public static RefreshTokenRedis fromDomain(RefreshToken token) {
        return RefreshTokenRedis.builder()
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
