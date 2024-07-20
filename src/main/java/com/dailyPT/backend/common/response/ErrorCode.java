package com.dailyPT.backend.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."), // 장애 상황
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),

    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),
    COMMON_ILLEGAL_STATUS("잘못된 상태값입니다."),
    COMMON_UNAUTHORIZED("인증 방식이 필요합니다."),
    COMMON_FORBIDDEN("로그인 된 유저의 권한으로는 해당 기능을 호출 할 수 없습니다."),
    COMMON_ACCESS_TOKEN_EXPIRE("액세스 토근이 만료되었습니다."),
    COMMON_CHANGED_USER_GRADE("사용자의 권한이 변경되었습니다.");

    private final String errorMsg;

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}
