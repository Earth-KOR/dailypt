package com.dailyPT.backend.config;

import lombok.Getter;

@Getter
public enum Headers {
    X_ID_VERIFY_RESULT("X-ID-VERIFY-RESULT"),
    AUTHORIZATION("Authorization");

    private String key;

    Headers(String key) { this.key = key; }
}
