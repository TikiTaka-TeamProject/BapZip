package com.sparta.bapzip.user.presentation.dto.response;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "유저 정보 수정 응답형식")
public class UserUpdateResponseDto {

    @Schema(description = "id", example = "1")
    private Long id;

    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "수정 시간", example = "2025-10-16T12:51:47.662241")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 유무", example = "false")
    private boolean isDeleted;

    public static UserUpdateResponseDto of(UserEntity user) {
        return UserUpdateResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .updatedAt(user.getUpdatedAt())
                .isDeleted(user.getIsDeleted())
                .build();
    }
}
