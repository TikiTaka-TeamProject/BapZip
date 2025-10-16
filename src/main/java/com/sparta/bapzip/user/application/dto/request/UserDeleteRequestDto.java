package com.sparta.bapzip.user.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "회원 삭제 요청 형식")
public class UserDeleteRequestDto {

    @Email
    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{8,16}$")
    @Schema(description = "비밀번호", example = "!Abc1234")
    private String password;
}
