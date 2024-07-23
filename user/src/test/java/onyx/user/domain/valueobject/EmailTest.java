package onyx.user.domain.valueobject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class EmailTest {

    @Test
    @DisplayName("Email객체를 생성한다.")
    void createEmail() {

        // GIVEN
        String localPart = "dnshewhsy";
        String domainPart = "google.com";
        String fullStringEmail = "dnshewhsy@google.com";

        // WHEN
        final Email createdEmail = Email.createFromFullAddress(fullStringEmail);

        // THEN
        assertThat(createdEmail.getEmailAddress()).isEqualTo(fullStringEmail);
        assertThat(createdEmail.getLocalPart()).isEqualTo(localPart);
        assertThat(createdEmail.getDomainPart()).isEqualTo(domainPart);

    }

    @Test
    @DisplayName("잘못된 주소로 Email 객체를 생성한다.")
    void createWrongEmail() {

        // GIVEN

        String withSpecialCharacter = "dnshe!whsy@google.com";
        String withDoubleAtCharacter = "dnshewhsy@@google.com";
        String withNotEnglishCharacter = "dnsheㅈwhsy@google.com";
        String withDoubleDotDomain = "dnshe!whsy@google..com";

        // WHEN & THEN

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                Email.createFromFullAddress(withSpecialCharacter));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                Email.createFromFullAddress(withDoubleAtCharacter));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                Email.createFromFullAddress(withNotEnglishCharacter));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                Email.createFromFullAddress(withDoubleDotDomain));
    }

}