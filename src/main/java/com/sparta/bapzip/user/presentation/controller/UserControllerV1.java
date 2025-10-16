package com.sparta.bapzip.user.presentation.controller;

import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.user.application.UserServiceV1;
import com.sparta.bapzip.user.application.dto.request.SignupRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserDeleteRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserRoleChangeRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserUpdateRequestDto;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import com.sparta.bapzip.user.presentation.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "회원 기능", description = "회원 기능 api")
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;

    @Operation(summary = "회원 가입", description = "회원 가입 메서드 입니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@RequestBody @Valid SignupRequestDto requestDto) {
       return ApiResponse.created(userServiceV1.signup(requestDto));
    }

    @Operation(summary = "전체 회원 조회", description = "전체 회원 조회 메서드 입니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getUserList(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                          @RequestParam(value = "size", defaultValue = "10") int size,
                                                                          @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                                          @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {
        return ApiResponse.ok(userServiceV1.getUserList(page - 1, size, sortBy, isAsc));
    }

    @Operation(summary = "단건 회원 조회", description = "단건 회원 조회 메서드 입니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable Long userId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(userServiceV1.getUser(userId, userDetails.getUser()));
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정 메서드 입니다.")
    @PatchMapping
    public ResponseEntity<ApiResponse<UserUpdateResponseDto>> updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto,
                                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(userServiceV1.updateUser(userUpdateRequestDto, userDetails.getUser()));
    }

    @Operation(summary = "회원 삭제", description = "회원 삭제 메서드 입니다.")
    @DeleteMapping
    public ResponseEntity<ApiResponse<UserDeleteResponseDto>> deleteUser(@RequestBody @Valid UserDeleteRequestDto userDeleteRequestDto,
                                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(userServiceV1.deleteUser(userDeleteRequestDto, userDetails.getUser()));
    }

    @Operation(summary = "회원 권한 변경", description = "회원 권한 변경 메서드 입니다.")
    @PatchMapping("/{userId}/role")
    public ResponseEntity<ApiResponse<UserRoleChangeResponseDto>> changeUserRole(@PathVariable Long userId,
                                                                                 @RequestBody @Valid UserRoleChangeRequestDto userRoleChangeRequestDto,
                                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(userServiceV1.changeUserRole(userId, userRoleChangeRequestDto, userDetails.getUser()));
    }
}
