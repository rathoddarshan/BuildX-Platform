package com.codingShuttle.projects.lovable.clone.service;

import com.codingShuttle.projects.lovable.clone.dto.subscription.PlanResponse;
import org.jspecify.annotations.Nullable;

public interface PlanService {
    PlanResponse getAllActivePlan();
}
