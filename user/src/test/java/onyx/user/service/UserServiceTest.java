package onyx.user.service;

import onyx.user.domain.entity.UserEntity;
import onyx.user.domain.valueobject.Email;
import onyx.user.domain.valueobject.OauthInfo;
import onyx.user.domain.valueobject.Provider;
import onyx.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 객체를 DB에 저장한다.")
    void createUserToDB() {

        // GIVEN
        final String nickName = "첫사용자";

        final String StringEmailAddress = "myfirstemail@google.com";
        final Email email = Email.createFromFullAddress(StringEmailAddress);

        final Provider provider = Provider.GOOGLE;
        final String providerId = "wqejwqeh2";
        final OauthInfo oauthInfo = OauthInfo.create(provider, providerId);

        final UserEntity userToSave = UserEntity.create(nickName, email, oauthInfo);

        // WHEN
        userService.register(userToSave);

        // THEN
        UserEntity savedUser = userRepository.findByEmail(Email.createFromFullAddress(StringEmailAddress)).orElseThrow();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getNickName()).isEqualTo(nickName);
        assertThat(savedUser.getEmail().getEmailAddress()).isEqualTo(StringEmailAddress);
        assertThat(savedUser.getOauthInfo().getProvider()).isEqualTo(provider);
        assertThat(savedUser.getPoints()).isZero();

    }

}