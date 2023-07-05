package univ.capston.bowling.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import univ.capston.bowling.domain.user.dto.PostSignUpReq;
import univ.capston.bowling.domain.user.dto.PostSignUpRes;
import univ.capston.bowling.domain.user.entity.UserEntity;
import univ.capston.bowling.domain.user.repository.UserRepository;
import univ.capston.bowling.global.config.error.exception.BaseException;
import univ.capston.bowling.global.util.BCrypt;

import static univ.capston.bowling.global.config.error.ErrorCode.BAD_REQUEST;
import static univ.capston.bowling.global.config.error.ErrorCode.INVALID_VALUE;
import static univ.capston.bowling.global.util.Regex.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public PostSignUpRes signUp(PostSignUpReq postSignUpReq) throws BaseException {
        // 아이디, 비밀번호, 닉네임 정규식 처리
        if(postSignUpReq.getUid().length() == 0 || postSignUpReq.getPassword().length() == 0 || postSignUpReq.getNickName().length() == 0 || postSignUpReq.getName().length() == 0){
            throw new BaseException(BAD_REQUEST);
        }
        if(!isValidUid(postSignUpReq.getUid())){
            throw new BaseException(INVALID_VALUE);
        }
        if(!isValidPassword(postSignUpReq.getPassword())){
            throw new BaseException(INVALID_VALUE);
        }
        if(!isValidNickName(postSignUpReq.getNickName())){
            throw new BaseException(INVALID_VALUE);
        }

        // 중복 아이디 체크
        if(userRepository.existsByUid(postSignUpReq.getUid())){
            throw new BaseException(INVALID_VALUE);
        }

        // 중복 닉네임 체크
        if(userRepository.existsByNickName(postSignUpReq.getNickName())){
            throw new BaseException(INVALID_VALUE);
        }

        // 비밀번호 암호화
        String pwd = BCrypt.encrypt(postSignUpReq.getPassword());

        UserEntity newUser = UserEntity.builder()
                .uid(postSignUpReq.getUid())
                .password(pwd)
                .name(postSignUpReq.getName())
                .nickName(postSignUpReq.getNickName())
                .build();

        userRepository.save(newUser);

        return new PostSignUpRes(newUser.getId());
    }
}
