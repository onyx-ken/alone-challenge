package onyx.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onyx.oauth.token.service.TokenService;
import onyx.user.domain.entity.UserEntity;
import onyx.user.service.UserAuthenticationService;
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

    private final UserAuthenticationService userAuthenticationService;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2UserInfo oAuth2UserInfo = extractOAuth2UserInfo((OAuth2AuthenticationToken) authentication);
        Optional<UserEntity> optionalUser = userAuthenticationService.findUserByOauthProviderId(oAuth2UserInfo.getProviderId());

        UserEntity userEntity = optionalUser
                .map(existingUser -> {
                    tokenService.revokeAccessToken(existingUser); // 기존 유저의 토큰 삭제
                    return existingUser; // 기존 유저 객체 반환
                })
                .orElseGet(() -> userAuthenticationService.register(UserEntity.fromOAuth2UserInfo(oAuth2UserInfo))); // 신규 유저 등록 및 객체 반환

        handleTokensAndRedirect(request, response, userEntity);
    }

    private OAuth2UserInfo extractOAuth2UserInfo(OAuth2AuthenticationToken token) {
        String provider = token.getAuthorizedClientRegistrationId();
        switch (provider) {
            case "google":
                log.info("구글 로그인 요청");
                return new GoogleUserInfo(token.getPrincipal().getAttributes());
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }

    private void handleTokensAndRedirect(HttpServletRequest request, HttpServletResponse response,
                                         UserEntity user) throws IOException {
        tokenService.issueAndSaveRefreshToken(user);
        String accessToken = tokenService.generateAccessToken(user);
        redirectToClient(request, response, user.getNickName(), accessToken);
    }

    private void redirectToClient(HttpServletRequest request, HttpServletResponse response,
                                  String nickName, String accessToken) throws IOException {
        String encodedName = URLEncoder.encode(nickName, StandardCharsets.UTF_8);
        String redirectUri = String.format(REDIRECT_URI, encodedName, accessToken);
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}
