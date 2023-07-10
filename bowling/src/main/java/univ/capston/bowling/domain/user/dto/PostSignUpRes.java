package univ.capston.bowling.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PostSignUpRes {
    private Long id;
    private String success="성공";
    public PostSignUpRes(Long id) {
        this.id = id;
    }
}
