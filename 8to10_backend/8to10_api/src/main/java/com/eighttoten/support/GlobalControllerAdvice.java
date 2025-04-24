package com.eighttoten.support;

import static com.eighttoten.exception.ExceptionCode.INTERNAL_SERVER_ERROR;
import static com.eighttoten.exception.ExceptionCode.INVALID_REQUEST;

import com.eighttoten.exception.AuthException;
import com.eighttoten.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request)
    {
        String errorMessage = createErrorMessage(ex.getBindingResult());
        log.warn(errorMessage, ex);

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(INVALID_REQUEST.getCode(), errorMessage));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeaderException(MissingRequestHeaderException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(INVALID_REQUEST.getCode(), "누락된 헤더 : " + e.getHeaderName()));
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingCookieException(MissingRequestCookieException e){
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(INVALID_REQUEST.getCode(), "누락된 쿠키 : " + e.getCookieName()));
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public void handleAsyncRequestNotUsableException(AsyncRequestNotUsableException e){
        // SSE 통신시 `DefaultHandlerExceptionResolver` 에서 발생하는 `AsyncRequestNotUsableException` 예외 경고 무시
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e){
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getExceptionCode().getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of(INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
    }

    private String createErrorMessage(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();

        bindingResult.getFieldErrors().forEach(fieldError ->
                sb.append(fieldError.getField())
                  .append(" 필드 검증 오류 : ")
                  .append(fieldError.getDefaultMessage())
                  .append(System.lineSeparator())
        );

        bindingResult.getGlobalErrors().forEach(globalError ->
                sb.append(" 객체 검증 오류 : ")
                  .append(globalError.getDefaultMessage())
                  .append(System.lineSeparator())
        );

        return sb.toString();
    }
}