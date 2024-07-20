package com.dailyPT.backend.user.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSampleResponse {
    @Schema(description = "유저 ID", example = "1")
    Long userId;
    @Schema(description = "유저 이름", example = "홍길동")
    String userName;
    @Schema(description = "유저 주소", example = "경기도 성남시")
    String address;
    @Schema(description = "유저 생성 날짜", example = "2024-07-22 12:00:00")
    String createdAt;
}
