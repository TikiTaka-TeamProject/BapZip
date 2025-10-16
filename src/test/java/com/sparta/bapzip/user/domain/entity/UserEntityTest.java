package com.sparta.bapzip.user.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.bapzip.user.application.dto.request.SignupRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserUpdateRequestDto;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserEntityTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("UserEntity 빌더 및 생성 테스트")
    void createUserEntity_withBuilder() {
        // given
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email("test@naver.com")
                .name("홍길동")
                .password("1234")
                .role(UserRoleEnum.CUSTOMER)
                .build();

        // when
        UserEntity user = UserEntity.create(requestDto, "encodedPassword");

        // then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@naver.com");
        assertThat(user.getName()).isEqualTo("홍길동");
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        assertThat(user.getRole()).isEqualTo(UserRoleEnum.CUSTOMER);
    }

    @Test
    @DisplayName("UserEntity 이름과 패스워드 업데이트")
    void updateUserEntity_nameAndPassword() {
        // given
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("홍길동")
                .email("test@naver.com")
                .password(passwordEncoder.encode("1234"))
                .role(UserRoleEnum.CUSTOMER)
                .build();

        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
                .name("김길동")
                .newPassword("5678")
                .build();

        // when
        user.update(requestDto, passwordEncoder);

        // then
        assertThat(user.getName()).isEqualTo("김길동");
        assertThat(passwordEncoder.matches("5678", user.getPassword())).isTrue();
    }

    @Test
    @DisplayName("UserEntity 패스워드가 null이면 기존 패스워드 유지")
    void updateUserEntity_passwordNull() {
        // given
        String originalPassword = passwordEncoder.encode("1234");
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("홍길동")
                .email("test@naver.com")
                .password(originalPassword)
                .role(UserRoleEnum.CUSTOMER)
                .build();

        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
                .name("김길동")
                .newPassword(null)
                .build();

        // when
        user.update(requestDto, passwordEncoder);

        // then
        assertThat(user.getName()).isEqualTo("김길동");
        assertThat(user.getPassword()).isEqualTo(originalPassword);
    }

    @Test
    @DisplayName("UserEntity 역할 변경 테스트")
    void changeRole() {
        // given
        UserEntity user = UserEntity.builder()
                .id(1L)
                .name("홍길동")
                .email("test@naver.com")
                .password("1234")
                .role(UserRoleEnum.CUSTOMER)
                .build();

        // when
        user.changeRole(UserRoleEnum.MANAGER);

        // then
        assertThat(user.getRole()).isEqualTo(UserRoleEnum.MANAGER);
    }
}
