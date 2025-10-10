package com.sparta.bapzip.user.application.dto.response;

import com.sparta.bapzip.user.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserUpdateResponseDto {

    private Long id;

    private String email;

    private String name;

    private LocalDateTime updatedAt;

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
