package com.codingShuttle.projects.lovable.clone.service;

import com.codingShuttle.projects.lovable.clone.dto.subscription.CheckoutRequest;
import com.codingShuttle.projects.lovable.clone.dto.subscription.CheckoutResponse;
import com.codingShuttle.projects.lovable.clone.dto.subscription.PortalResponse;
import com.codingShuttle.projects.lovable.clone.dto.subscription.SubscriptionResponse;
import org.jspecify.annotations.Nullable;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId);

    PortalResponse openCustomerPortal(Long userId);
}
