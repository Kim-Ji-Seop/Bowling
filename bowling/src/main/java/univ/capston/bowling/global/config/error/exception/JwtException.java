package univ.capston.bowling.global.config.error.exception;

import lombok.Getter;
import univ.capston.bowling.global.config.error.ErrorCode;

@Getter
public class JwtException extends RuntimeException {
    private final ErrorCode jwtErrorCode;
    private final int code;
    private final String errorMessage;

    // Without Cause Exception
    public JwtException(ErrorCode errorcode) {
        super(errorcode.getMessage());
        this.jwtErrorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorcode.getMessage();
    }

    public JwtException(ErrorCode errorcode, String errorMessage) {
        super(errorMessage);
        this.jwtErrorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorMessage;
    }

    // With Cause Exception
    public JwtException(ErrorCode errorcode, Exception cause) {
        super(errorcode.getMessage(), cause);
        this.jwtErrorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorcode.getMessage();
    }

    public JwtException(ErrorCode errorcode, String errorMessage, Exception cause) {
        super(errorMessage, cause);
        this.jwtErrorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorMessage;
    }
}
