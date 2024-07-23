package onyx.user.domain.entity;

import onyx.user.domain.valueobject.Email;
import onyx.user.domain.valueobject.OauthInfo;
import onyx.user.domain.valueobject.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class UserEntityTest {

    @Test
    @DisplayName("처음 사용자를 생성 한다.")
    void createUser() {

        // GIVEN

        String nickName = "운영자";
        final Email email = Email.createFromFullAddress("dnshewhsy@google.com");
        final OauthInfo oauthInfo = OauthInfo.create(Provider.GOOGLE, "qwjsndwj23w");

        // WHEN
        final UserEntity userEntity = UserEntity.create(nickName, email, oauthInfo);

        // THEN
        assertThat(userEntity.getNickName()).isEqualTo(nickName);
        assertThat(userEntity.getEmail().getEmailAddress()).isEqualTo("dnshewhsy@google.com");
        assertThat(userEntity.oauthInfo.getProvider()).isEqualTo(Provider.GOOGLE);
        assertThat(userEntity.oauthInfo.getProviderId()).isEqualTo("qwjsndwj23w");
        assertThat(userEntity.getPoints()).isZero();

    }

}