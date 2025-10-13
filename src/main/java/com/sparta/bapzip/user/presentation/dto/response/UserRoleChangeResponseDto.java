package com.sparta.bapzip.user.presentation.dto.response;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserRoleChangeResponseDto {

    private Long id;

    private String email;

    private String name;

    private UserRoleEnum role;

    private LocalDateTime updatedAt;

    private Long updatedBy;

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
