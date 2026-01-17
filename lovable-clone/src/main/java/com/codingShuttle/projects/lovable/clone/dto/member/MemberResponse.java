package com.codingShuttle.projects.lovable.clone.dto.member;

import com.codingShuttle.projects.lovable.clone.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String email,
        String name,
        String avatarUrl,
        ProjectRole role,
        Instant invitedAt
) {
}
