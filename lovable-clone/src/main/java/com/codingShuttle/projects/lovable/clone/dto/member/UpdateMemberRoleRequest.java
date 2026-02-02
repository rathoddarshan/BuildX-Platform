package com.codingShuttle.projects.lovable.clone.dto.member;

import com.codingShuttle.projects.lovable.clone.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
        @NotNull ProjectRole role
) {
}
