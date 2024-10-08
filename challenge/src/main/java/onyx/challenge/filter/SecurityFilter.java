package onyx.challenge.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Profile({"dev", "prod"})
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String userIdHeader = request.getHeader("X-User-Id");

        // 외부 요청에서 X-User-Id 헤더를 설정하지 못하도록 검증
        if (userIdHeader != null && request.getHeader("Authorization") == null) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Forbidden header");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
