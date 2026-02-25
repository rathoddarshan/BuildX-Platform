package com.codingShuttle.projects.buildX.platform.dto.project;

import com.codingShuttle.projects.buildX.platform.dto.auth.UserProfileResponse;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        UserProfileResponse owner
) {
}
