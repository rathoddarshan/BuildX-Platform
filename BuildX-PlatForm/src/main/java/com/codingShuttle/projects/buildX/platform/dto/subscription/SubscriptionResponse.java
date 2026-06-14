package com.codingShuttle.projects.buildX.platform.dto.subscription;

import com.codingShuttle.projects.buildX.platform.service.SubscriptionService;

import java.time.Instant;

public record SubscriptionResponse(
        PlanResponse plan,
        String status,
        Instant currentPeriodEnd,
        Long tokenUsedThisCycle,
        SubscriptionService subscriptionService
) {
}
