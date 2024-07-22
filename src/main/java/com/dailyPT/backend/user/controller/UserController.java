package com.dailyPT.backend.user.controller;

import com.dailyPT.backend.common.response.CommonResponse;
import com.dailyPT.backend.config.JwtTokenProvider;
import com.dailyPT.backend.config.SwaggerConfig;
import com.dailyPT.backend.user.controller.dto.request.AddUserRequest;
import com.dailyPT.backend.user.controller.dto.request.UserLoginRequest;
import com.dailyPT.backend.user.controller.dto.response.UserSampleResponse;
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

    @Operation(summary = "전체 유저 조회")
    @GetMapping("")
    public CommonResponse<Object> getUser() {
        return CommonResponse.success(userService.getUser());
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public CommonResponse<Object> login(@RequestBody UserLoginRequest userLoginRequest) {
        return CommonResponse.success(userService.findById(userLoginRequest.getUserId()));
    }

    @Operation(summary = "토큰 @AuthenticationPrincipal 테스트")
    @GetMapping("/token")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CommonResponse<Object> tokenTest(@AuthenticationPrincipal Long userId) {
        return CommonResponse.success(userId);
    }

    @Operation(summary = "FFD 샘플 API")
    @GetMapping("/sample")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CommonResponse<Object> sample() {
        return CommonResponse.success(new UserSampleResponse(
                1L,
                "테스트",
                "서울특별시 어딘가~",
                "2024-07-31 18:00:00"
        ));
    }

    @Operation(summary = "배포 테스트")
    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CommonResponse<Object> test() {
        return null;
    }

    @Operation(summary = "배포 테스트")
    @GetMapping("/test2")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CommonResponse<Object> test2() {
        return null;
    }
}

