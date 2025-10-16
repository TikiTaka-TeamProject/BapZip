package com.sparta.bapzip.user.application.dto.request;

import com.sparta.bapzip.user.domain.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "권한 변경 요청 형식")
public class UserRoleChangeRequestDto {

    @NotNull
    @Schema(description = "권한", example = "CUSTOMER")
    private UserRoleEnum role;
}
