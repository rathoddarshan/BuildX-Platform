package com.codingShuttle.projects.buildX.platform.dto.subscription;

public record PlanLimitResponse(
        String planName,
        Integer maxTokensPerDay,
        Integer maxProject,
        Boolean unlimitedAi
) {
}
