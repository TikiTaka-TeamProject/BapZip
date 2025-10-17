package com.sparta.bapzip.user.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bapzip.global.exception.ErrorCode;
import com.sparta.bapzip.user.application.UserServiceV1;
import com.sparta.bapzip.user.application.dto.request.SignupRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserDeleteRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserRoleChangeRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserUpdateRequestDto;
import com.sparta.bapzip.user.application.excpetion.DuplicateUserException;
import com.sparta.bapzip.user.application.excpetion.PasswordNotMatchException;
import com.sparta.bapzip.user.application.excpetion.UnauthorizedUserException;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import com.sparta.bapzip.user.domain.entity.UserEntity;
import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import com.sparta.bapzip.user.presentation.dto.response.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(UserControllerV1.class)
class UserControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserServiceV1 userServiceV1;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Nested
    @DisplayName("회원가입 테스트")
    class signupTests {

        @Test
        @DisplayName("회원가입 성공")
        void signup_success() throws Exception {
            // given
            SignupRequestDto requestDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            SignupResponseDto responseDto = SignupResponseDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            // service mock 설정
            when(userServiceV1.signup(any(SignupRequestDto.class))).thenReturn(responseDto);

            // when & then
            mockMvc.perform(post("/v1/users/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                    .andExpect(jsonPath("$.data.name").value("홍길동"))
                    .andExpect(jsonPath("$.data.role").value("CUSTOMER"));
        }

        @Test
        @DisplayName("회원가입 실패 - 이메일 중복")
        void signup_duplicateEmail() throws Exception {
            // given
            SignupRequestDto requestDto = SignupRequestDto.builder()
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            // 서비스 레이어에서 예외 발생하도록 Mock 설정
            when(userServiceV1.signup(any(SignupRequestDto.class)))
                    .thenThrow(new DuplicateUserException(ErrorCode.DUPLICATE_USER));

            // when & then
            mockMvc.perform(post("/v1/users/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isConflict()) // 중복 이메일이면 409 Conflict
                    .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_USER.getMessage()));
        }

        @Test
        @DisplayName("회원가입 실패 - 유효하지 않은 이메일")
        void signup_invalidEmail() throws Exception {
            SignupRequestDto requestDto = SignupRequestDto.builder()
                    .email("invalid-email") // 잘못된 이메일
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            mockMvc.perform(post("/v1/users/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest()); // 400
        }
    }

    @Nested
    @DisplayName("유저 목록 조회 테스트")
    class getUserListTests {

        @Test
        @DisplayName("유저 목록 조회 성공")
        void getUserList_success() throws Exception {
            // given
            UserResponseDto user1 = UserResponseDto.builder()
                    .id(1L)
                    .email("user1@naver.com")
                    .name("홍길동1")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserResponseDto user2 = UserResponseDto.builder()
                    .id(2L)
                    .email("user2@naver.com")
                    .name("홍길동2")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            List<UserResponseDto> users = List.of(user1, user2);

            // Mock Page 객체
            Page<UserResponseDto> pageResult = new PageImpl<>(users);

            when(userServiceV1.getUserList(0, 10, "createdAt", false)).thenReturn(pageResult);

            // when & then
            mockMvc.perform(get("/v1/users")
                            .param("page", "1")
                            .param("size", "10")
                            .param("sortBy", "createdAt")
                            .param("isAsc", "false")
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.items[0].id").value(1))
                    .andExpect(jsonPath("$.data.items[0].email").value("user1@naver.com"))
                    .andExpect(jsonPath("$.data.items[0].name").value("홍길동1"))
                    .andExpect(jsonPath("$.data.items[0].role").value("CUSTOMER"))
                    .andExpect(jsonPath("$.data.items[1].id").value(2))
                    .andExpect(jsonPath("$.data.items[1].email").value("user2@naver.com"))
                    .andExpect(jsonPath("$.data.items[1].name").value("홍길동2"))
                    .andExpect(jsonPath("$.data.items[1].role").value("CUSTOMER"))
                    .andExpect(jsonPath("$.data.totalElements").value(2))
                    .andExpect(jsonPath("$.data.currentPage").value(1))
                    .andExpect(jsonPath("$.data.pageSize").value(2))
                    .andExpect(jsonPath("$.data.sortBy").value("created_at"));
        }

        @Test
        @DisplayName("유저 목록 조회 - 페이지 파라미터 기본값")
        void getUserList_defaultParams() throws Exception {
            // given
            List<UserResponseDto> users = List.of();

            Page<UserResponseDto> pageResult = new PageImpl<>(users);

            when(userServiceV1.getUserList(0, 10, "createdAt", false)).thenReturn(pageResult);

            // when & then
            mockMvc.perform(get("/v1/users")
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.items").isEmpty())
                    .andExpect(jsonPath("$.data.totalElements").value(0))
                    .andExpect(jsonPath("$.data.currentPage").value(1))
                    .andExpect(jsonPath("$.data.pageSize").value(0));
        }
    }

    @Nested
    @DisplayName("특정 유저 조회 테스트")
    class getUserTests {

        @Test
        @DisplayName("유저 조회 성공 - 본인 정보")
        void getUser_self_success() throws Exception {
            Long userId = 1L;

            UserEntity mockUser = UserEntity.builder()
                    .id(userId)
                    .email("user@naver.com")
                    .name("홍길동")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            // UserDetailsImpl이 권한 반환하도록 설정
            UserDetailsImpl userDetails = new UserDetailsImpl(mockUser);

            // 권한 포함 Authentication
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            UserResponseDto responseDto = UserResponseDto.builder()
                    .id(userId)
                    .email("user@naver.com")
                    .name("홍길동")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            // 서비스 Mock
            when(userServiceV1.getUser(eq(userId), eq(mockUser)))
                    .thenReturn(responseDto);

            mockMvc.perform(get("/v1/users/{userId}", userId)
                            .with(authentication(authToken))
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.id").value(userId))
                    .andExpect(jsonPath("$.data.email").value("user@naver.com"))
                    .andExpect(jsonPath("$.data.name").value("홍길동"))
                    .andExpect(jsonPath("$.data.role").value("CUSTOMER"));
        }

        @Test
        @DisplayName("유저 조회 성공 - 권한 있는 관리자")
        void getUser_other_success_withManager() throws Exception {
            Long targetUserId = 2L;

            UserEntity managerUser = UserEntity.builder()
                    .id(1L)
                    .email("manager@naver.com")
                    .name("관리자")
                    .role(UserRoleEnum.MANAGER)
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(managerUser);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            UserResponseDto responseDto = UserResponseDto.builder()
                    .id(targetUserId)
                    .email("other@naver.com")
                    .name("홍길동")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            when(userServiceV1.getUser(eq(targetUserId), eq(managerUser)))
                    .thenReturn(responseDto);

            mockMvc.perform(get("/v1/users/{userId}", targetUserId)
                            .with(authentication(authToken))
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.id").value(targetUserId))
                    .andExpect(jsonPath("$.data.email").value("other@naver.com"))
                    .andExpect(jsonPath("$.data.name").value("홍길동"))
                    .andExpect(jsonPath("$.data.role").value("CUSTOMER"));
        }

        @Test
        @DisplayName("유저 조회 실패 - 권한 없음")
        void getUser_unauthorized() throws Exception {
            Long targetUserId = 2L;

            UserEntity normalUser = UserEntity.builder()
                    .id(1L)
                    .email("user@naver.com")
                    .name("홍길동")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserDetailsImpl userDetails = new UserDetailsImpl(normalUser);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            when(userServiceV1.getUser(eq(targetUserId), eq(normalUser)))
                    .thenThrow(new UnauthorizedUserException(ErrorCode.UNAUTHORIZED_USER));

            mockMvc.perform(get("/v1/users/{userId}", targetUserId)
                            .with(authentication(authToken))
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("회원 정보 수정 테스트")
    class updateUserTests {

        @Test
        @DisplayName("회원 정보 수정 성공")
        void updateUser_success() throws Exception {
            // given
            UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
                    .name("홍길동")
                    .password("12345678")
                    .newPassword(null)
                    .build();

            UserUpdateResponseDto responseDto = UserUpdateResponseDto.builder()
                    .id(1L)
                    .name("홍길동")
                    .build();

            // service mock 설정
            when(userServiceV1.updateUser(any(UserUpdateRequestDto.class), any(UserEntity.class)))
                    .thenReturn(responseDto);

            // 인증 객체 생성
            UserEntity mockUser = UserEntity.builder()
                    .id(1L)
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserDetailsImpl authUser = new UserDetailsImpl(mockUser);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());

            // when & then
            mockMvc.perform(patch("/v1/users")
                            .with(authentication(authToken))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.id").value(1L))
                    .andExpect(jsonPath("$.data.name").value("홍길동"));
        }

        @Test
        @DisplayName("회원 정보 수정 실패 - 비밀번호 불일치")
        void updateUser_passwordMismatch() throws Exception {
            // given
            UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
                    .name("홍길동")
                    .password("wrongpassword")
                    .newPassword(null)
                    .build();

            when(userServiceV1.updateUser(any(UserUpdateRequestDto.class), any(UserEntity.class)))
                    .thenThrow(new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH));

            UserEntity mockUser = UserEntity.builder()
                    .id(1L)
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserDetailsImpl authUser = new UserDetailsImpl(mockUser);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());

            // when & then
            mockMvc.perform(patch("/v1/users")
                            .with(authentication(authToken))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value(ErrorCode.PASSWORD_NOT_MATCH.getMessage()));
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class deleteUserTests {

        @Test
        @DisplayName("회원 탈퇴 성공")
        void deleteUser_success() throws Exception {
            // given
            UserDeleteRequestDto requestDto = UserDeleteRequestDto.builder()
                    .password("12345678")
                    .build();

            UserDeleteResponseDto responseDto = UserDeleteResponseDto.builder()
                    .id(1L)
                    .name("홍길동")
                    .isDeleted(true)
                    .build();

            // service mock 설정
            when(userServiceV1.deleteUser(any(UserDeleteRequestDto.class), any(UserEntity.class)))
                    .thenReturn(responseDto);

            // 인증 객체 생성
            UserEntity mockUser = UserEntity.builder()
                    .id(1L)
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserDetailsImpl authUser = new UserDetailsImpl(mockUser);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());

            // when & then
            mockMvc.perform(delete("/v1/users")
                            .with(authentication(authToken))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.id").value(1L))
                    .andExpect(jsonPath("$.data.name").value("홍길동"))
                    .andExpect(jsonPath("$.data.deleted").value(true));
        }

        @Test
        @DisplayName("회원 탈퇴 실패 - 비밀번호 불일치")
        void deleteUser_passwordMismatch() throws Exception {
            // given
            UserDeleteRequestDto requestDto = UserDeleteRequestDto.builder()
                    .password("wrongpassword")
                    .build();

            when(userServiceV1.deleteUser(any(UserDeleteRequestDto.class), any(UserEntity.class)))
                    .thenThrow(new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH));

            UserEntity mockUser = UserEntity.builder()
                    .id(1L)
                    .email("test@naver.com")
                    .name("홍길동")
                    .password("12345678")
                    .role(UserRoleEnum.CUSTOMER)
                    .build();

            UserDetailsImpl authUser = new UserDetailsImpl(mockUser);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());

            // when & then
            mockMvc.perform(delete("/v1/users")
                            .with(authentication(authToken))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value(ErrorCode.PASSWORD_NOT_MATCH.getMessage()));
        }
    }

    @Nested
    @DisplayName("유저 권한 변경 테스트")
    class changeUserRoleTests {

        @Test
        @DisplayName("유저 권한 변경 성공")
        void changeUserRole_success() throws Exception {
            // given
            Long targetUserId = 2L;

            UserRoleChangeRequestDto requestDto = UserRoleChangeRequestDto.builder()
                    .role(UserRoleEnum.MANAGER)
                    .build();

            UserRoleChangeResponseDto responseDto = UserRoleChangeResponseDto.builder()
                    .id(targetUserId)
                    .name("홍길동")
                    .role(UserRoleEnum.MANAGER)
                    .build();

            when(userServiceV1.changeUserRole(anyLong(), any(UserRoleChangeRequestDto.class), any(UserEntity.class)))
                    .thenReturn(responseDto);

            // 인증 객체
            UserEntity authUser = UserEntity.builder()
                    .id(1L)
                    .email("admin@naver.com")
                    .name("관리자")
                    .password("admin1234")
                    .role(UserRoleEnum.MASTER)
                    .build();
            UserDetailsImpl userDetails = new UserDetailsImpl(authUser);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // when & then
            mockMvc.perform(patch("/v1/users/{userId}/role", targetUserId)
                            .with(authentication(authToken))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.id").value(2L))
                    .andExpect(jsonPath("$.data.name").value("홍길동"))
                    .andExpect(jsonPath("$.data.role").value("MANAGER"));
        }

        @Test
        @DisplayName("유저 권한 변경 실패 - MASTER 권한 변경 불가")
        void changeUserRole_fail_masterRole() throws Exception {
            Long targetUserId = 2L;

            UserRoleChangeRequestDto requestDto = UserRoleChangeRequestDto.builder()
                    .role(UserRoleEnum.MASTER)
                    .build();

            when(userServiceV1.changeUserRole(anyLong(), any(UserRoleChangeRequestDto.class), any(UserEntity.class)))
                    .thenThrow(new UnauthorizedUserException(ErrorCode.UNAUTHORIZED_USER));

            UserEntity authUser = UserEntity.builder()
                    .id(1L)
                    .email("admin@naver.com")
                    .name("관리자")
                    .password("admin1234")
                    .role(UserRoleEnum.MASTER)
                    .build();
            UserDetailsImpl userDetails = new UserDetailsImpl(authUser);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            mockMvc.perform(patch("/v1/users/{userId}/role", targetUserId)
                            .with(authentication(authToken))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value(ErrorCode.UNAUTHORIZED_USER.getMessage()));
        }
    }
}
