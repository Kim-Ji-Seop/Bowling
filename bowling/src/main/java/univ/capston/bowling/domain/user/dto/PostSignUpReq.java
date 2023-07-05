package univ.capston.bowling.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PostSignUpReq {
    private String uid;
    private String password;
    private String name;
    private String nickName;
    public PostSignUpReq() {}
}
