package univ.capston.bowling.domain.user.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserEntity {

    @Id
    private Long userIdx;
}
