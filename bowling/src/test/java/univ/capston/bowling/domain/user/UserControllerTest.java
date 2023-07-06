package univ.capston.bowling.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import univ.capston.bowling.domain.user.dto.PostSignUpReq;
import univ.capston.bowling.domain.user.dto.PostSignUpRes;
import univ.capston.bowling.domain.user.service.UserService;
import univ.capston.bowling.global.config.error.ErrorCode;
import univ.capston.bowling.global.config.error.exception.BaseException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private PostSignUpReq postSignUpReq;
    private PostSignUpRes postSignUpRes;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        postSignUpReq = new PostSignUpReq();
        postSignUpReq.setUid("teamjskim2x");
        postSignUpReq.setPassword("Kjs997743@@");
        postSignUpReq.setName("김지섭");
        postSignUpReq.setNickName("섭섭");

        postSignUpRes = new PostSignUpRes(1L);

        objectMapper = new ObjectMapper();
    }

    @Test
    void signUpSuccess() throws Exception {
        when(userService.signUp(postSignUpReq)).thenReturn(postSignUpRes);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postSignUpReq)))
                .andExpect(status().isOk());
    }

    @Test
    void signUpFailure() throws Exception {
        when(userService.signUp(postSignUpReq)).thenThrow(new BaseException(ErrorCode.INVALID_VALUE));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postSignUpReq)))
                .andExpect(status().isBadRequest());
    }
}

