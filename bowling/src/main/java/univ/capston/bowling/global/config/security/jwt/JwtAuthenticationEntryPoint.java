package univ.capston.bowling.global.config.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
// 이 클래스는 Spring의 컴포넌트로 등록되며, 인증되지 않은 사용자에게 보여줄 진입점을 정의.
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // 인증되지 않은 사용자가 보호된 자원에 접근을 시도할 때 호출되는 메서드.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 인증되지 않은 사용자에게 401 Unauthorized 상태 코드와 "Unauthorized" 메시지를 반환.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
