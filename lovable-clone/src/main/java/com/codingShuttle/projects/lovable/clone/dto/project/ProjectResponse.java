package com.codingShuttle.projects.lovable.clone.dto.project;

import com.codingShuttle.projects.lovable.clone.dto.auth.UserProfileResponse;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        Instant createdAt,
        UserProfileResponse owner
) {
}
