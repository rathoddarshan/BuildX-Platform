package com.codingShuttle.projects.buildX.platform.dto.member;

import com.codingShuttle.projects.buildX.platform.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
        @NotNull ProjectRole role
) {
}
