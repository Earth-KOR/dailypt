package com.dailyPT.backend.common.exception;

import org.springframework.http.HttpStatus;

public class JwtTokenNotValidateException extends BusinessException {

    public JwtTokenNotValidateException() {
    }

    public JwtTokenNotValidateException(String message) {
        super(message);
    }

    public JwtTokenNotValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtTokenNotValidateException(Throwable cause) {
        super(cause);
    }

    public JwtTokenNotValidateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public boolean isNecessaryToLog() {
        return false;
    }
}
