package com.dailyPT.backend.common.exception;

import org.springframework.http.HttpStatus;

public class GradeChangeException extends BusinessException {

    public GradeChangeException() {
    }

    public GradeChangeException(String message) {
        super(message);
    }

    public GradeChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GradeChangeException(Throwable cause) {
        super(cause);
    }

    public GradeChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
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
