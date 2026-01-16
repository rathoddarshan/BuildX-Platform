package com.codingShuttle.projects.lovable.clone.dto.project;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String name,
        Instant createdAt
) {
}
