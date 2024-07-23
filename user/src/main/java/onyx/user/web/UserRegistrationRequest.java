package onyx.user.web;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String nickName;
    private String emailAddress;
    private String provider;
    private String providerId;
}
