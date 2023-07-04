package univ.capston.bowling.global.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import univ.capston.bowling.global.config.error.ErrorCode;
import univ.capston.bowling.global.config.error.exception.BaseException;

import javax.security.auth.Subject;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String JWT_SECRET = "VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa";

    private long tokenValidTime = 30 * 60 * 1000L;

    // 토큰 유효시간
    private final int JWT_EXPIRATION_MS = 1000 * 60 * 60 * 24*14;

    // jwt 토큰 생성
    public TokenDto generateToken(Authentication authentication) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        String accessToken = Jwts.builder()
                .setSubject((String) authentication.getPrincipal()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                // 1일 : 1000 * 60 * 60 * 24
                .setExpiration(new Date(now.getTime()+1000 * 60*60*24))
                .signWith(SignatureAlgorithm.HS256,JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject((String) authentication.getPrincipal()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                // expiryDate
                .setExpiration(expiryDate) // 만료 시간 세팅
                .signWith(SignatureAlgorithm.HS256,JWT_SECRET)
                .compact();
        DecodedJWT decodedJWT= JWT.decode(refreshToken);

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // refresh token으로 accessToken 재발급
    public TokenDto reissueAtk(String userEmail,String rtkEmail) throws JsonProcessingException {

        if(!rtkEmail.equals(userEmail)){
            throw new BaseException(ErrorCode.EXPIRED_AUTHENTICATION);
        }
        Authentication authentication=new UsernamePasswordAuthenticationToken(userEmail,null,null);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        String accessToken=Jwts.builder()
                .setSubject((String) authentication.getPrincipal()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(new Date(now.getTime()+1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();

        String refreshToken=Jwts.builder()
                .setSubject((String) authentication.getPrincipal()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(expiryDate) // 만료 시간 세팅
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();
        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(JWT_SECRET).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    // Jwt 토큰에서 아이디 추출
    public static String getUserEmailFromJWT(String token) {


        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Subject getSubject(String accessToken) throws JsonProcessingException {
        String subjectStr = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(accessToken).getBody().getSubject();
        return new ObjectMapper().readValue(subjectStr, Subject.class);
    }
}
