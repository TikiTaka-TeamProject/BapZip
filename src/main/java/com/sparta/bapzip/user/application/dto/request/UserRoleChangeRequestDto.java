package com.sparta.bapzip.user.application.dto.request;

import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRoleChangeRequestDto {

    @NotNull
    private UserRoleEnum role;
}
