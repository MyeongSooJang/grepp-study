package com.grepp.jms.greppstudy.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "로그인 이메일")
        String email,
        @Schema(description = "로그인 비밀번호")
        String password
) {
}
