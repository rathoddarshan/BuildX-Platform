package com.codingShuttle.projects.buildX.platform.dto.subscription;

public record PlanResponse(
        Long id,
        String name,
        Integer MaxProject,
        Integer MaxToken,
        Boolean unlimitedAi,
        String price
) {
}
