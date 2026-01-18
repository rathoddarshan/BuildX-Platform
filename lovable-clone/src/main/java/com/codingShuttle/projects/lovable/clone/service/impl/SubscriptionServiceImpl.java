package com.codingShuttle.projects.lovable.clone.service.impl;

import com.codingShuttle.projects.lovable.clone.dto.subscription.CheckoutRequest;
import com.codingShuttle.projects.lovable.clone.dto.subscription.CheckoutResponse;
import com.codingShuttle.projects.lovable.clone.dto.subscription.PortalResponse;
import com.codingShuttle.projects.lovable.clone.dto.subscription.SubscriptionResponse;
import com.codingShuttle.projects.lovable.clone.service.SubscriptionService;
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
