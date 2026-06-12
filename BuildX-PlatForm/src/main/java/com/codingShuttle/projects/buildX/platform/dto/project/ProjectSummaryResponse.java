package com.codingShuttle.projects.buildX.platform.dto.project;

import com.codingShuttle.projects.buildX.platform.enums.ProjectRole;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String projectName,
        Instant createdAt,
        ProjectRole role
) {
}
