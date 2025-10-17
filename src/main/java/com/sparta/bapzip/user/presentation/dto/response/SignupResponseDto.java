package com.sparta.bapzip.user.presentation.dto.response;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "회원가입 응답형식")
public class SignupResponseDto {

    @NotBlank
    @Email
    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @NotBlank
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotBlank
    @Schema(description = "권한", example = "CUSTOMER")
    private UserRoleEnum role;

    public static SignupResponseDto of(UserEntity userEntity) {
        return SignupResponseDto.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .role(userEntity.getRole())
                .build();
    }
}
