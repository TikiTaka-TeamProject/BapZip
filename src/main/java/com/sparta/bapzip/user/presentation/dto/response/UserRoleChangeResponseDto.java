package com.sparta.bapzip.user.presentation.dto.response;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "유저 권한 변경 응답형식")
public class UserRoleChangeResponseDto {

    @Schema(description = "id", example = "1")
    private Long id;

    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "권한", example = "MANAGER")
    private UserRoleEnum role;

    @Schema(description = "수정 시간", example = "2025-10-16T12:51:47.662241")
    private LocalDateTime updatedAt;

    @Schema(description = "수정자", example = "1")
    private Long updatedBy;

    @Schema(description = "삭제 유무", example = "false")
    private boolean isDeleted;

    public static UserRoleChangeResponseDto of(UserEntity user) {
        return UserRoleChangeResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .updatedAt(user.getUpdatedAt())
                .updatedBy(user.getUpdatedBy())
                .isDeleted(user.getIsDeleted())
                .build();
    }
}
