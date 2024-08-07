package onyx.oauth;

import onyx.user.domain.valueobject.Provider;

public interface OAuth2UserInfo {
    String getProviderId();
    Provider getProvider();
    String getName();
    String getEmail();
}
