package show.schedulemanagement.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    private final int code;
    private final String message;

    public AuthException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
