package com.codingShuttle.projects.buildX.platform.dto.member;

import com.codingShuttle.projects.buildX.platform.enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
        @Email @NotBlank String username,
        @NotNull ProjectRole role
) {

}
