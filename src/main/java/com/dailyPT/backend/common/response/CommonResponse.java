package com.dailyPT.backend.common.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    @Getter
    @NoArgsConstructor
    public static class Error {
        private String fieldName;
        private String message;

        public Error(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }

        public static List<Error> of(BindingResult bindingResult){
            return bindingResult.getAllErrors().stream()
                    .map(error -> new Error(
                                    ((FieldError) error).getField()
                                    , error.getDefaultMessage()
                            )
                    ).toList();
        }
    }

    public enum Result {
        SUCCESS, FAIL
    }

    public enum ErrorType {
        ERROR, WARNING
    }

    private Result result;
    private T data;
    private String message;
    private String errorCode;
    private ErrorType errorType;

    public static <T> CommonResponse<T> success(T data, String message) {
        return (CommonResponse<T>) CommonResponse.builder()
                .result(Result.SUCCESS)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> success(T data) {
        return success(data, null);
    }

    public static CommonResponse<Object> fail(String message, String errorCode, ErrorType errorType) {
        return CommonResponse.builder()
                .result(Result.FAIL)
                .message(message)
                .errorCode(errorCode)
                .errorType(errorType)
                .build();
    }

    public static CommonResponse<Object> fail(ErrorCode errorCode) {
        return CommonResponse.builder()
                .result(Result.FAIL)
                .message(errorCode.getErrorMsg())
                .errorCode(errorCode.name())
                .build();
    }

    public static CommonResponse<Object> fail(List<Error> bindingResult)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return CommonResponse.fail(mapper.writeValueAsString(bindingResult), ErrorCode.COMMON_INVALID_PARAMETER.name(), ErrorType.ERROR);
    }


}
