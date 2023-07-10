package univ.capston.bowling.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.capston.bowling.global.config.BaseResponse;
import univ.capston.bowling.global.config.BaseException;

import static univ.capston.bowling.global.config.BaseResponseStatus.BAD_REQUEST;
import static univ.capston.bowling.global.config.BaseResponseStatus.SUCCESS;

@RestController
public class UserController {
    @Value("${spring.profiles.active:}")
    private String activeProfile;
    // 배포...
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
