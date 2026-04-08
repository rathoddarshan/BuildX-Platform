package com.codingShuttle.projects.buildX.platform.dto.subscription;

public record PlanResponse(
        Long id,
        String name,
        Integer maxProject,
        Integer maxToken,
        Boolean unlimitedAi,
        String price
) {
}
