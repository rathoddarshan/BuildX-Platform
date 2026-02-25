package com.codingShuttle.projects.buildX.platform.dto.project;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String projectName,
        Instant createdAt
) {
}
