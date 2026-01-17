package com.codingShuttle.projects.lovable.clone.dto.member;

import com.codingShuttle.projects.lovable.clone.enums.ProjectRole;

public record InviteMemberRequest(
        String name,
        ProjectRole role
) {
}
