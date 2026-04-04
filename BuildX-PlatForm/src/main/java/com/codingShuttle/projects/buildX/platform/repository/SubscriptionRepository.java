package com.codingShuttle.projects.buildX.platform.repository;

import com.codingShuttle.projects.buildX.platform.dto.subscription.SubscriptionResponse;
import com.codingShuttle.projects.buildX.platform.entity.Subscription;
import com.codingShuttle.projects.buildX.platform.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<SubscriptionStatus> active);
}
