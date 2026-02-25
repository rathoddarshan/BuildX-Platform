package com.codingShuttle.projects.buildX.platform.service.impl;

import com.codingShuttle.projects.buildX.platform.dto.subscription.CheckoutRequest;
import com.codingShuttle.projects.buildX.platform.dto.subscription.CheckoutResponse;
import com.codingShuttle.projects.buildX.platform.dto.subscription.PortalResponse;
import com.codingShuttle.projects.buildX.platform.dto.subscription.SubscriptionResponse;
import com.codingShuttle.projects.buildX.platform.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        return null;
    }

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
