package univ.capston.bowling.global.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import univ.capston.bowling.global.config.error.BaseResponse;
import univ.capston.bowling.global.config.error.ErrorCode;
import univ.capston.bowling.global.config.redis.RedisDao;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisDao redisDao;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 회원가입 API는 JWT 토큰이 없어도 접근 가능해야 하므로, 토큰 체크를 건너뛰는 로직 추가
        if ("/users".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        ErrorCode errorCode=null;
        String jwt = getJwtFromRequest(request); //request에서 jwt 토큰을 꺼낸다.

        if(jwt==null){
            errorCode=ErrorCode.TOKEN_NOT_EXIST;
            sendErrorResponse(response,ErrorCode.TOKEN_NOT_EXIST);
        }
        else  {

            try{
                //Redis 에 해당 accessToken logout 여부 확인
                String isLogout = redisDao.getValues(jwt);
                if (ObjectUtils.isEmpty(isLogout)) {
                    String userEmail = JwtTokenProvider.getUserEmailFromJWT(jwt); //jwt에서 사용자 id를 꺼낸다.

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, null, null); //id를 인증한다.
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //기본적으로 제공한 details 세팅
                    SecurityContextHolder.getContext().setAuthentication(authentication); //세션에서 계속 사용하기 위해 securityContext에 Authentication 등록


                }
                filterChain.doFilter(request, response);



                // catch 구문 jwt 토큰 유효성 검사
            }catch (IllegalArgumentException e) {
                log.error("an error occured during getting username from token", e);
                // JwtException (custom exception) 예외 발생시키기
                sendErrorResponse(response,ErrorCode.INVALID_TOKEN);
            } catch (ExpiredJwtException e) {
                log.warn("the token is expired and not valid anymore", e);
                sendErrorResponse(response,ErrorCode.ACCESS_TOKEN_EXPIRED);

            } catch(SignatureException e){
                log.error("Authentication Failed. Username or Password not valid.");
                sendErrorResponse(response,ErrorCode.FAIL_AUTHENTICATION);
            }catch(UnsupportedJwtException e){
                log.error("UnsupportedJwt");
                sendErrorResponse(response,ErrorCode.FAIL_AUTHENTICATION);
            }
        }
    }
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse httpServletResponse,ErrorCode errorCode) throws IOException{


        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType(APPLICATION_JSON_VALUE);

        BaseResponse errorResponse = new BaseResponse(errorCode);
        //object를 텍스트 형태의 JSON으로 변환
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorResponse);
    }
}
