package com.codingShuttle.projects.buildX.platform.dto.member;

import com.codingShuttle.projects.buildX.platform.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        String avatarUrl,
        ProjectRole projectRole,
        Instant invitedAt
) {
}
