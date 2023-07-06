package univ.capston.bowling.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import univ.capston.bowling.domain.user.dto.PostSignUpReq;
import univ.capston.bowling.domain.user.dto.PostSignUpRes;
import univ.capston.bowling.domain.user.service.UserService;
import univ.capston.bowling.global.config.error.BaseResponse;
import univ.capston.bowling.global.config.error.ErrorCode;
import univ.capston.bowling.global.config.error.exception.BaseException;
//import univ.capston.bowling.global.config.redis.RedisDao;
//import univ.capston.bowling.global.config.security.jwt.JwtAuthenticationFilter;
//import univ.capston.bowling.global.config.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final RedisDao redisDao;

    @PostMapping("")
    public ResponseEntity<BaseResponse<PostSignUpRes>> signUp(@RequestBody PostSignUpReq postSignUpReq) {
        try {
            PostSignUpRes postSignUpRes = userService.signUp(postSignUpReq);
            return ResponseEntity.ok(new BaseResponse<>(postSignUpRes));
        } catch (BaseException baseException) {
            ErrorCode errorCode = baseException.getErrorCode();
            return ResponseEntity.status(errorCode.getHttpStatus())
                    .body(new BaseResponse<>(errorCode.getMessage(), errorCode.getCode()));
        }
    }
}
