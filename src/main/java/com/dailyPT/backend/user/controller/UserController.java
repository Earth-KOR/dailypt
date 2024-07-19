package com.dailyPT.backend.user.controller;

import com.dailyPT.backend.common.response.CommonResponse;
import com.dailyPT.backend.config.JwtTokenProvider;
import com.dailyPT.backend.config.SwaggerConfig;
import com.dailyPT.backend.user.controller.dto.AddUserRequest;
import com.dailyPT.backend.user.controller.dto.UserLoginRequest;
import com.dailyPT.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = SwaggerConfig.SwaggerTags.USER)
@RestController
@RequestMapping("/dailyPT/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "유저 추가")
    @PostMapping("")
    public CommonResponse<Object> addUser(@RequestBody AddUserRequest addUserRequest) {
        userService.addUser(addUserRequest.getName(), addUserRequest.getRole());
        return CommonResponse.success(null);
    }

    @Operation(summary = "유저 전체 조회")
    @GetMapping("")
    public CommonResponse<Object> getUser() {
        return CommonResponse.success(userService.getUser());
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public CommonResponse<Object> login(@RequestBody UserLoginRequest userLoginRequest) {
        return CommonResponse.success(userService.findById(userLoginRequest.getUserId()));
    }

    @Operation(summary = "토큰 테스트")
    @GetMapping("/token")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CommonResponse<Object> tokenTest(@AuthenticationPrincipal Long userId) {
        return CommonResponse.success(userId);
    }

    @Operation(summary = "배포 테스트")
    @GetMapping("/token2")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CommonResponse<Object> tokenTest2(@AuthenticationPrincipal Long userId) {
        return CommonResponse.success(userId);
    }
}

