package univ.capston.bowling.domain.user.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserEntityTest {

    @Test
    public void testUserEntityCreation() {
        // Arrange
        Long id = 1L;
        String uid = "user1";
        String password = "password";
        String name = "John Doe";
        String nickName = "johndoe";

        // Act
        UserEntity userEntity = new UserEntity(id, uid, password, name, nickName);

        // Assert
        Assertions.assertEquals(id, userEntity.getId());
        Assertions.assertEquals(uid, userEntity.getUid());
        Assertions.assertEquals(password, userEntity.getPassword());
        Assertions.assertEquals(name, userEntity.getName());
        Assertions.assertEquals(nickName, userEntity.getNickName());
    }

    // Add more test cases as needed for other scenarios

}
