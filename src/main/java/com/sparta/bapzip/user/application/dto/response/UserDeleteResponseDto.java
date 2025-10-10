package com.sparta.bapzip.user.application.dto.response;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDeleteResponseDto {

    private Long id;

    private String email;

    private String name;

    private LocalDateTime deletedAt;

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
