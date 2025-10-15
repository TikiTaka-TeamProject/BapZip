package com.sparta.bapzip.user.application;

import com.sparta.bapzip.user.application.dto.request.SignupRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserDeleteRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserRoleChangeRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserUpdateRequestDto;
import com.sparta.bapzip.user.application.excpetion.DuplicateUserException;
import com.sparta.bapzip.user.application.excpetion.PasswordNotMatchException;
import com.sparta.bapzip.user.application.excpetion.UnauthorizedUserException;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import com.sparta.bapzip.user.domain.repository.UserRepository;
import com.sparta.bapzip.user.presentation.dto.response.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class UserServiceV1Test {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceV1 userServiceV1;

    @Nested
    @DisplayName("회원가입 테스트")
    class signupTests {

        @Test
        @DisplayName("회원가입 성공")
        void signup() {
            //given
            SignupRequestDto requestDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            String encodedPassword = "encoded_12345678";
            UserEntity fakeUser = UserEntity.create(requestDto, encodedPassword);

            when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            when(userRepository.save(any(UserEntity.class))).thenReturn(fakeUser);

            // when
            SignupResponseDto responseDto = userServiceV1.signup(requestDto);

            //then
            assertThat(responseDto.getEmail()).isEqualTo(requestDto.getEmail());
            assertThat(responseDto.getName()).isEqualTo(requestDto.getName());
            assertThat(responseDto.getRole()).isEqualTo(requestDto.getRole());
        }

        @Test
        @DisplayName("중복 이메일로 회원가입 시 DuplicateUserException 발생")
        void signup_duplicateEmail() {
            // given
            SignupRequestDto requestDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserEntity existingUser = UserEntity.create(requestDto, "encoded_pw");

            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

            // when & then
            assertThrows(DuplicateUserException.class, () -> {
                userServiceV1.signup(requestDto);
            });
        }

        @Test
        @DisplayName("MANAGER 권한으로 회원가입 시 UnauthorizedUserException 발생")
        void signup_unauthorizedRole() {
            // given
            SignupRequestDto requestDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("관리자")
                    .password("12345678")
                    .role(UserRoleEnum.MANAGER)
                    .build();

            // when & then
            assertThrows(UnauthorizedUserException.class, () -> {
                userServiceV1.signup(requestDto);
            });
        }
    }

    @Nested
    @DisplayName("유저 전체 조회 테스트")
    class GetUserListTests {

        @Test
        @DisplayName("정상적으로 페이지를 가져올 수 있다 (ASC 정렬)")
        void getUserList_success() {
            // given
            int page = 0;
            int size = 10;
            String sortBy = "createdAt";
            boolean isAsc = true;

            SignupRequestDto requestDto1 = SignupRequestDto.builder()
                    .email("test1@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            SignupRequestDto requestDto2 = SignupRequestDto.builder()
                    .email("test2@naver.com")
                    .name("김철수")
                    .password("12345678")
                    .role(UserRoleEnum.MANAGER)
                    .build();

            UserEntity user1 = UserEntity.create(requestDto1, "encoded_pw");
            UserEntity user2 = UserEntity.create(requestDto2, "encoded_pw");

            List<UserEntity> userList = List.of(user1, user2);
            Page<UserEntity> mockPage = new PageImpl<>(userList);

            when(userRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

            // when
            Page<UserResponseDto> result = userServiceV1.getUserList(page, size, sortBy, isAsc);

            // then
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getContent().get(0).getEmail()).isEqualTo("test1@naver.com");
            assertThat(result.getContent().get(1).getName()).isEqualTo("김철수");
        }

        @Test
        @DisplayName("잘못된 size 값(예: 15)을 주면 기본값 10으로 대체된다")
        void getUserList_invalidSizeDefaultsTo10() {
            // given
            int page = 0;
            int size = 15; // 유효하지 않은 값
            String sortBy = "email";
            boolean isAsc = false;

            when(userRepository.findAll(any(Pageable.class)))
                    .thenAnswer(invocation -> {
                        Pageable pageable = invocation.getArgument(0);
                        assertThat(pageable.getPageSize()).isEqualTo(10);
                        return new PageImpl<>(List.of());
                    });

            // when
            Page<UserResponseDto> result = userServiceV1.getUserList(page, size, sortBy, isAsc);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("회원 단건 조회 테스트")
    class GetUserTests {

        @Test
        @DisplayName("요청한 유저 ID가 토큰 유저 ID와 같으면 자신의 정보 반환")
        void getUser_sameUser_returnsSelfInfo() {
            // given
            SignupRequestDto requestDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserEntity user = UserEntity.create(requestDto, "encoded_pw");
            ReflectionTestUtils.setField(user, "id", 1L);

            // when
            UserResponseDto response = userServiceV1.getUser(1L, user);

            // then
            assertThat(response.getEmail()).isEqualTo("test@naver.com");
            assertThat(response.getName()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("MANAGER 또는 MASTER는 다른 유저의 정보 조회 가능")
        void getUser_managerOrMaster_canViewOthers() {
            // given
            SignupRequestDto managerDto = SignupRequestDto.builder()
                    .email("manager@naver.com")
                    .name("관리자")
                    .password("12345678")
                    .role(UserRoleEnum.MANAGER)
                    .build();

            UserEntity manager = UserEntity.create(managerDto, "encoded_pw");
            ReflectionTestUtils.setField(manager, "id", 10L);

            SignupRequestDto targetDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserEntity targetUser = UserEntity.create(targetDto, "encoded_pw");
            ReflectionTestUtils.setField(targetUser, "id", 20L);

            when(userRepository.findById(20L)).thenReturn(Optional.of(targetUser));

            // when
            UserResponseDto response = userServiceV1.getUser(20L, manager);

            // then
            assertThat(response.getEmail()).isEqualTo("test@naver.com");
            assertThat(response.getName()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("CUSTOMER가 다른 유저 정보 요청 시 UnauthorizedUserException 발생")
        void getUser_customerCannotAccessOthers() {
            // given
            SignupRequestDto customerDto = SignupRequestDto.builder()
                    .email("customer@test.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserEntity customer = UserEntity.create(customerDto, "encoded_pw");
            ReflectionTestUtils.setField(customer, "id", 20L);


            assertThrows(UnauthorizedUserException.class, () -> {
                userServiceV1.getUser(99L, customer);
            });
        }
    }

    @Nested
    @DisplayName("회원정보 수정 테스트")
    class UpdateUserTests {

        @Test
        @DisplayName("비밀번호가 일치하면 유저 정보가 정상적으로 수정된다")
        void updateUser_success() {
            // given
            SignupRequestDto signupDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserEntity user = UserEntity.create(signupDto, "encoded_oldPw");
            ReflectionTestUtils.setField(user, "id", 1L);

            UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                    .password("12345678") // 현재 비밀번호 확인용
                    .newPassword("87654321")
                    .name("김철수")
                    .build();

            // mock 설정
            when(passwordEncoder.matches("12345678", "encoded_oldPw")).thenReturn(true);
            when(passwordEncoder.encode("87654321")).thenReturn("encoded_newPw");
            when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // when
            UserUpdateResponseDto response = userServiceV1.updateUser(updateRequest, user);

            // then
            assertThat(response.getName()).isEqualTo("김철수");
            assertThat(user.getPassword()).isEqualTo("encoded_newPw");
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 PasswordNotMatchException 발생")
        void updateUser_invalidPassword() {
            // given
            SignupRequestDto signupDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserEntity user = UserEntity.create(signupDto, "encoded_oldPw");
            ReflectionTestUtils.setField(user, "id", 2L);

            UserUpdateRequestDto updateRequest = UserUpdateRequestDto.builder()
                    .password("wrongPw") // 틀린 비밀번호
                    .newPassword("newPw")
                    .name("김철수")
                    .build();

            // mock 설정
            when(passwordEncoder.matches("wrongPw", "encoded_oldPw")).thenReturn(false);

            // when & then
            assertThrows(PasswordNotMatchException.class, () -> {
                userServiceV1.updateUser(updateRequest, user);
            });
        }
    }

    @Nested
    @DisplayName("유저 삭제 테스트")
    class DeleteUserTests {

        @Test
        @DisplayName("비밀번호가 일치하면 유저 삭제 처리 후 DTO 반환")
        void deleteUser_success() {
            // given
            SignupRequestDto signupDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserEntity user = UserEntity.create(signupDto, "encoded_oldPw");
            ReflectionTestUtils.setField(user, "id", 1L);

            UserDeleteRequestDto deleteRequest = UserDeleteRequestDto.builder()
                    .password("12345678")
                    .build();

            // mock 설정
            when(passwordEncoder.matches("12345678", "encoded_oldPw")).thenReturn(true);
            when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // when
            UserDeleteResponseDto response = userServiceV1.deleteUser(deleteRequest, user);

            // then
            assertThat(response.getEmail()).isEqualTo("test@naver.com");
            assertThat(user.getIsDeleted()).isTrue(); // markDeleted가 true로 바뀌었는지 확인
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 PasswordNotMatchException 발생")
        void deleteUser_invalidPassword() {
            // given
            SignupRequestDto signupDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserEntity user = UserEntity.create(signupDto, "encoded_oldPw");
            ReflectionTestUtils.setField(user, "id", 2L);

            UserDeleteRequestDto deleteRequest = UserDeleteRequestDto.builder()
                    .password("wrongPw")
                    .build();

            // mock 설정
            when(passwordEncoder.matches("wrongPw", "encoded_oldPw")).thenReturn(false);

            // when & then
            assertThrows(PasswordNotMatchException.class, () -> {
                userServiceV1.deleteUser(deleteRequest, user);
            });
        }
    }

    @Nested
    @DisplayName("유저 권한 변경 테스트")
    class ChangeUserRoleTests {

        @Test
        @DisplayName("정상적으로 ROLE 변경 후 DTO 반환")
        void changeUserRole_success() {
            UserEntity targetUser = UserEntity.create(
                    SignupRequestDto.builder()
                            .email("test@naver.com")
                            .name("홍길동")
                            .password("pw")
                            .role(UserRoleEnum.CUSTOMER)
                            .build(),
                    "encoded_pw");
            ReflectionTestUtils.setField(targetUser, "id", 2L);

            UserEntity adminUser = UserEntity.create(
                    SignupRequestDto.builder()
                            .email("admin@naver.com")
                            .name("관리자")
                            .password("pw")
                            .role(UserRoleEnum.MASTER)
                            .build(),
                    "encoded_pw");
            ReflectionTestUtils.setField(adminUser, "id", 1L);

            UserRoleChangeRequestDto changeRequest = UserRoleChangeRequestDto.builder()
                    .role(UserRoleEnum.MANAGER)
                    .build();

            when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));

            UserRoleChangeResponseDto response = userServiceV1.changeUserRole(2L, changeRequest, adminUser);

            assertThat(response.getRole()).isEqualTo(UserRoleEnum.MANAGER);
        }

        @Test
        @DisplayName("MASTER로 변경 시도 시 UnauthorizedUserException 발생")
        void changeUserRole_masterNotAllowed() {
            // given
            SignupRequestDto targetDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("pw")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserEntity targetUser = UserEntity.create(targetDto, "encoded_pw");
            ReflectionTestUtils.setField(targetUser, "id", 2L);

            SignupRequestDto requestDto = SignupRequestDto.builder()
                    .email("admin@naver.com")
                    .name("관리자")
                    .password("pw")
                    .role(UserRoleEnum.MANAGER)
                    .build();

            UserEntity requester = UserEntity.create(requestDto, "encoded_pw");
            ReflectionTestUtils.setField(requester, "id", 1L);

            UserRoleChangeRequestDto changeRequest = UserRoleChangeRequestDto.builder()
                    .role(UserRoleEnum.MASTER)
                    .build();

            // when & then
            assertThrows(UnauthorizedUserException.class, () -> {
                userServiceV1.changeUserRole(2L, changeRequest, requester);
            });
        }
    }
}
