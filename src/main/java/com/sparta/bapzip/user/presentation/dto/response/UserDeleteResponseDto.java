package com.sparta.bapzip.user.presentation.dto.response;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "회원삭제 응답형식")
public class UserDeleteResponseDto {

    @Schema(description = "id", example = "1")
    private Long id;

    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "삭제 시간", example = "2025-10-16T12:41:05.83905")
    private LocalDateTime deletedAt;

    @Schema(description = "삭제 유무", example = "true")
    private boolean isDeleted;

    public static UserDeleteResponseDto of(UserEntity user) {
        return UserDeleteResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .deletedAt(user.getDeletedAt())
                .isDeleted(user.getIsDeleted())
                .build();
    }
}
