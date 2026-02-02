package com.codingShuttle.projects.lovable.clone.dto.member;

import com.codingShuttle.projects.lovable.clone.enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest(
        @Email @NotBlank String email,
        @NotNull ProjectRole role
) {

}
