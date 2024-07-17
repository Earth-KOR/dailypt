package com.dailyPT.backend.common.exception;

import com.dailyPT.backend.common.response.CommonResponse;
import com.dailyPT.backend.common.response.CommonResponse.ErrorType;
import com.dailyPT.backend.common.response.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.dailyPT.backend.common.response.CommonResponse.Error.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 예상하지 못한 error는 공통 Error Message
    @ResponseBody
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<Object> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return CommonResponse.fail(exception.getMessage(), ErrorCode.COMMON_SYSTEM_ERROR.name(), ErrorType.ERROR);
    }

    // 비즈니스 로직상 예상 할 수 있는 에러는 커스터마이징 Error Message
    @ResponseBody
    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<Object> BusinessException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return CommonResponse.fail(exception.getMessage(), ErrorCode.COMMON_ILLEGAL_STATUS.name(), ErrorType.WARNING);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected CommonResponse<Object> handle(MethodArgumentNotValidException exception)
            throws JsonProcessingException {
        return CommonResponse.fail(of(exception.getBindingResult()));
    }

    // 잘못된 parameter를 보낼 때
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            IllegalArgumentException.class,
            MissingServletRequestParameterException.class,
            HttpRequestMethodNotSupportedException.class,
            RequestRejectedException.class,
            MissingRequestHeaderException.class,
            HttpMessageNotReadableException.class,
            TypeMismatchException.class
    })
    protected CommonResponse<Object> handle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return CommonResponse.fail(exception.getMessage(), ErrorCode.COMMON_INVALID_PARAMETER.name(), ErrorType.ERROR);
    }

    // 인증값이 없을 때
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResponse<Object> handleAuthenticationException(AuthenticationException exception) {
        return CommonResponse.fail(exception.getMessage(), ErrorCode.COMMON_UNAUTHORIZED.name(), ErrorType.ERROR);
    }

    // 권한에 맞지 않는 호출이 발생 할 때
    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected CommonResponse<Object> handleAccessDeniedException(AccessDeniedException exception) {
        return CommonResponse.fail(ErrorCode.COMMON_FORBIDDEN.getErrorMsg(), ErrorCode.COMMON_FORBIDDEN.name(), ErrorType.ERROR);
    }
}
