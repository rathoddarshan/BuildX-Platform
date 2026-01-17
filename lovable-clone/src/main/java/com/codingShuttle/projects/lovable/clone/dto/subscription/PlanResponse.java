package com.codingShuttle.projects.lovable.clone.dto.subscription;

public record PlanResponse(
        Long id,
        String name,
        Integer MaxProject,
        Integer MaxToken,
        Boolean unlimitedAi,
        String price
) {
}
