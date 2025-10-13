package com.sparta.bapzip.user.presentation.dto.response;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponseDto {

    private Long id;

    private String name;

    private String email;

    private UserRoleEnum role;

    private LocalDateTime createdAt;

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
