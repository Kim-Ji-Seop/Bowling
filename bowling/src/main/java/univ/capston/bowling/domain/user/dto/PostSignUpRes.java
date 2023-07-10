package univ.capston.bowling.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PostSignUpRes {
    private Long id;
    public PostSignUpRes(Long id) {
        this.id = id;
    }
}
