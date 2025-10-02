package com.sparta.bapzip.user.domain.entity;

import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.user.presentation.dto.request.SignupRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public static UserEntity create(SignupRequestDto requestDto, String encodingPassword) {
        return UserEntity.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .password(encodingPassword)
                .role(requestDto.getRole())
                .build();
    }
}
