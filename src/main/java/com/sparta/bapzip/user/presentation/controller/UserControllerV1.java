package com.sparta.bapzip.user.presentation.controller;

import com.sparta.bapzip.global.response.ApiResponse;
import com.sparta.bapzip.user.application.UserServiceV1;
import com.sparta.bapzip.user.application.dto.request.SignupRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserDeleteRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserRoleChangeRequestDto;
import com.sparta.bapzip.user.application.dto.request.UserUpdateRequestDto;
import com.sparta.bapzip.user.application.dto.response.*;
import com.sparta.bapzip.user.domain.entity.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserControllerV1 {

    private final UserServiceV1 userServiceV1;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@RequestBody @Valid SignupRequestDto requestDto) {
       return ApiResponse.created(userServiceV1.signup(requestDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getUserList(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                          @RequestParam(value = "size", defaultValue = "10") int size,
                                                                          @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                                          @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {
        return ApiResponse.ok(userServiceV1.getUserList(page - 1, size, sortBy, isAsc));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable Long userId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(userServiceV1.getUser(userId, userDetails.getUser()));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserUpdateResponseDto>> updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto,
                                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(userServiceV1.updateUser(userUpdateRequestDto, userDetails.getUser()));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<UserDeleteResponseDto>> deleteUser(@RequestBody @Valid UserDeleteRequestDto userDeleteRequestDto,
                                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(userServiceV1.deleteUser(userDeleteRequestDto, userDetails.getUser()));
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<ApiResponse<UserRoleChangeResponseDto>> changeUserRole(@PathVariable Long userId,
                                                                                 @RequestBody @Valid UserRoleChangeRequestDto userRoleChangeRequestDto,
                                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(userServiceV1.changeUserRole(userId, userRoleChangeRequestDto, userDetails.getUser()));
    }
}
