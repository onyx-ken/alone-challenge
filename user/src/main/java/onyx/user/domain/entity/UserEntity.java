package onyx.user.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.user.domain.valueobject.Email;
import onyx.user.domain.valueobject.OauthInfo;
import onyx.user.domain.valueobject.Profile;

@Entity
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName;

    private Email email;

    @Embedded
    OauthInfo oauthInfo;

    @Embedded
    Profile profile;

    private int points = 0;

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    private UserEntity(String nickName, Email email,
                      OauthInfo oauthInfo) {
        this.nickName = nickName;
        this.email = email;
        this.oauthInfo = oauthInfo;
    }

    public static UserEntity create(String nickName, Email emailAddress,
                                    OauthInfo oauthInfo) {
        return new UserEntity(nickName, emailAddress, oauthInfo);
    }


}
