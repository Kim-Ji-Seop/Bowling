package univ.capston.bowling.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.capston.bowling.domain.user.dto.PostSignUpReq;
import univ.capston.bowling.domain.user.dto.PostSignUpRes;
import univ.capston.bowling.domain.user.service.UserService;
import univ.capston.bowling.global.config.BaseResponse;
import univ.capston.bowling.global.config.BaseException;
import univ.capston.bowling.global.config.BaseResponseStatus;

import static univ.capston.bowling.global.config.BaseResponseStatus.BAD_REQUEST;
import static univ.capston.bowling.global.config.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @Value("${spring.profiles.active:}")
    private String activeProfile;
    @PostMapping("/users")
    public ResponseEntity<BaseResponse<PostSignUpRes>> signUp(@RequestBody PostSignUpReq postSignUpReq) {
        try {
            PostSignUpRes postSignUpRes = userService.signUp(postSignUpReq);
            return ResponseEntity.ok(new BaseResponse<>(postSignUpRes));
        } catch (BaseException baseException) {
            BaseResponseStatus errorCode = baseException.getStatus();
            return ResponseEntity.status(errorCode.getHttpStatus())
                    .body(new BaseResponse<>(errorCode.getMessage(), errorCode.getCode()));
        }
    }
    @GetMapping("/user")
    public ResponseEntity<BaseResponse> userTest(){
        try{
            return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
        } catch(BaseException exception){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse<>(BAD_REQUEST));
        }
    }
    @GetMapping("")
    public String helloWorld(){
        return "hello world";
    }

    @GetMapping("/profile")
    public String profile(){
        return activeProfile;
    }

}
