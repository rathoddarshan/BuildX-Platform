package com.codingShuttle.projects.lovable.clone.dto.subscription;

public record PlanLimitResponse(
        String planName,
        Integer maxTokensPerDay,
        Integer maxProject,
        Boolean unlimitedAi
) {
}
