package onyx.user.domain.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthInfo {

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId;

    public static OauthInfo create(Provider provider, String providerId) {
        return new OauthInfo(provider, providerId);
    }

    private OauthInfo(Provider provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }

}
