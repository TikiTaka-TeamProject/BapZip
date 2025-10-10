package com.sparta.bapzip.user.domain.entity;

import com.sparta.bapzip.global.common.BaseEntity;
import com.sparta.bapzip.user.application.dto.request.UserUpdateRequestDto;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import com.sparta.bapzip.user.application.dto.request.SignupRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "p_users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PROTECTED)
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

    public void update(UserUpdateRequestDto requestDto, PasswordEncoder passwordEncoder) {
        this.name = requestDto.getName() == null ? this.name : requestDto.getName();
        this.password = requestDto.getNewPassword() == null ? this.password : passwordEncoder.encode(requestDto.getNewPassword());
    }
}
