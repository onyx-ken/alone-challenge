package onyx.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onyx.oauth.jwt.JwtUtil;
import onyx.oauth.token.domain.RefreshToken;
import onyx.oauth.token.service.RefreshTokenService;
import onyx.user.domain.entity.UserEntity;
import onyx.user.domain.valueobject.Email;
import onyx.user.domain.valueobject.OauthInfo;
import onyx.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.redirect}")
    private String REDIRECT_URI;

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2UserInfo oAuth2UserInfo = extractOAuth2UserInfo((OAuth2AuthenticationToken) authentication);
        Optional<UserEntity> optionalUser = userService.findUserByOauthProviderId(oAuth2UserInfo.getProviderId());

        UserEntity userEntity = optionalUser
                .map(user -> updateUser(user, oAuth2UserInfo))
                .orElseGet(() -> registerNewUser(oAuth2UserInfo));

        handleTokensAndRedirect(request, response, userEntity);
    }

    private OAuth2UserInfo extractOAuth2UserInfo(OAuth2AuthenticationToken token) {
        String provider = token.getAuthorizedClientRegistrationId();
        switch (provider) {
            case "google":
                log.info("구글 로그인 요청");
                return new GoogleUserInfo(token.getPrincipal().getAttributes());
            // 다른 provider 추가 가능
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }

    private UserEntity updateUser(UserEntity existingUser, OAuth2UserInfo oAuth2UserInfo) {
        log.info("기존 유저입니다.");
        refreshTokenService.deleteRefreshTokenByUserId(existingUser.getId());
        return existingUser;
    }

    private UserEntity registerNewUser(OAuth2UserInfo oAuth2UserInfo) {
        log.info("신규 유저입니다. 등록을 진행합니다.");
        Email email = Email.createFromFullAddress(oAuth2UserInfo.getEmail());
        OauthInfo oauthInfo = OauthInfo.create(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
        UserEntity userEntity = UserEntity.create(oAuth2UserInfo.getName(), email, oauthInfo);
        return userService.register(userEntity);
    }

    private void handleTokensAndRedirect(HttpServletRequest request, HttpServletResponse response,
                                         UserEntity user) throws IOException {
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), REFRESH_TOKEN_EXPIRATION_TIME);
        saveRefreshToken(user, refreshToken);

        String accessToken = jwtUtil.generateAccessToken(user.getId(), ACCESS_TOKEN_EXPIRATION_TIME);
        redirectToClient(request, response, user.getNickName(), accessToken, refreshToken);
    }

    private void saveRefreshToken(UserEntity user, String refreshToken) {
        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .build();
        refreshTokenService.issueRefreshToken(newRefreshToken);
    }

    private void redirectToClient(HttpServletRequest request, HttpServletResponse response,
                                  String nickName, String accessToken, String refreshToken) throws IOException {
        String encodedName = URLEncoder.encode(nickName, StandardCharsets.UTF_8);
        String redirectUri = String.format(REDIRECT_URI, encodedName, accessToken, refreshToken);
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}
