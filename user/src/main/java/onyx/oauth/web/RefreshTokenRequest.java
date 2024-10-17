package onyx.oauth.web;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
