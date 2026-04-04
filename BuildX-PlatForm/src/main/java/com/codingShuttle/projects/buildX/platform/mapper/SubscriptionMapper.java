package com.codingShuttle.projects.buildX.platform.mapper;

import com.codingShuttle.projects.buildX.platform.dto.subscription.PlanResponse;
import com.codingShuttle.projects.buildX.platform.dto.subscription.SubscriptionResponse;
import com.codingShuttle.projects.buildX.platform.entity.Plan;
import com.codingShuttle.projects.buildX.platform.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);
}
