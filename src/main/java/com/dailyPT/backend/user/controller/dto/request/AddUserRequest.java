package com.dailyPT.backend.user.controller.dto.request;

import lombok.Getter;

@Getter
public class AddUserRequest {
    String name;
    String role;
}