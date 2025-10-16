package com.sparta.bapzip.user.presentation.dto.response;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "회원정보 조회 응답형식")
public class UserResponseDto {

    @Schema(description = "id", example = "1")
    private Long id;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @Schema(description = "권한", example = "CUSTOMER")
    private UserRoleEnum role;

    @Schema(description = "생성 시간", example = "2025-10-16T12:51:47.662241")
    private LocalDateTime createdAt;

    @Schema(description = "삭제 유무", example = "false")
    private boolean isDeleted;

    public static UserResponseDto of(UserEntity user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .isDeleted(user.getIsDeleted())
                .build();
    }
}
