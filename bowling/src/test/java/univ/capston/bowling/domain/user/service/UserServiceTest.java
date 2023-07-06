package univ.capston.bowling.domain.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import univ.capston.bowling.domain.user.dto.PostSignUpReq;
import univ.capston.bowling.domain.user.dto.PostSignUpRes;
import univ.capston.bowling.domain.user.entity.UserEntity;
import univ.capston.bowling.domain.user.repository.UserRepository;
import univ.capston.bowling.domain.user.service.UserService;
import univ.capston.bowling.global.config.error.exception.BaseException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    public void testSignUp_Success() throws BaseException {
        // Arrange
        PostSignUpReq signUpReq = new PostSignUpReq();
        signUpReq.setUid("teamjskim2x");
        signUpReq.setPassword("Kjs997743@@");
        signUpReq.setName("김지섭");
        signUpReq.setNickName("섭섭");

        when(userRepository.existsByUid("teamjskim2x")).thenReturn(false);
        when(userRepository.existsByNickName("섭섭")).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity(1L, "user1", "hashed_password", "John Doe", "johndoe"));

        // Act
        PostSignUpRes signUpRes = userService.signUp(signUpReq);

        // Assert
        Assertions.assertEquals(1L, signUpRes.getId());
        verify(userRepository, times(1)).existsByUid("user1");
        verify(userRepository, times(1)).existsByNickName("johndoe");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testSignUp_InvalidRequestParameters() {
        // Arrange
        PostSignUpReq signUpReq = new PostSignUpReq();
        signUpReq.setUid("");
        signUpReq.setPassword("");
        signUpReq.setName("");
        signUpReq.setNickName("");

        // Act & Assert
        Assertions.assertThrows(BaseException.class, () -> userService.signUp(signUpReq));
        verify(userRepository, never()).existsByUid(anyString());
        verify(userRepository, never()).existsByNickName(anyString());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    // Add more test cases as needed for other scenarios

}