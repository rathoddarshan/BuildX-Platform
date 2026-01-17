package com.codingShuttle.projects.lovable.clone.dto.subscription;

public record PlanLimitResponse(
        String planName,
        int maxTokensPerDay,
        int maxProject,
        boolean unlimitedAi
) {
}
