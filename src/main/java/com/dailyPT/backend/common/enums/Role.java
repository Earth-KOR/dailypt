package com.dailyPT.backend.common.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    ADMIN("ROLE_ADMIN", "관리자"),
    NOT("ROLE_NOT", "권한 없음");

    private final String userGrade;
    private final String description;

    @Override
    public String getAuthority() {
        return userGrade;
    }
}
