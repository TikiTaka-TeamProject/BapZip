package com.sparta.bapzip.user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "로그인 요청 형식")
public class LoginRequestDto {

    @NotBlank
    @Email
    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @NotBlank
    @Schema(description = "비밀번호", example = "!Abc1234")
    private String password;
}
