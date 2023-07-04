package univ.capston.bowling.global.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisDao {
    private final RedisTemplate<String, Object> redisTemplate;

    // 주어진 키-값 쌍을 Redis에 저장
    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }
    //        redisTemplate.opsForValue()
//                .set("RT:" + authentication.getName(),tokenDto.getRefreshToken(), Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())), TimeUnit.MILLISECONDS);
    // 인증 객체의 이름을 키로, 리프레시 토큰을 값으로 사용하여 Redis에 저장합니다. 또한, 이 키-값 쌍은 주어진 시간(밀리세컨드 단위) 동안만 유효하게 설정됩니다.
    public void setValues(Authentication authentication, String refreshToken, Long time) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(authentication.getName(),refreshToken,time , TimeUnit.MILLISECONDS);
    }

    // 주어진 키에 대응하는 값을 Redis에서 검색하여 반환합니다.
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        return (String) values.get(key);
    }

    // 주어진 키와 그에 대응하는 값을 Redis에서 삭제합니다.
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
