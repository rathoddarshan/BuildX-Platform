package com.codingShuttle.projects.buildX.platform.dto.subscription;

import java.time.Instant;

public record SubscriptionResponse(
        PlanResponse plan,
        String status,
        Instant periodEnd,
        Long tokenUsedThisCycle
) {
}
