package com.codingShuttle.projects.buildX.platform.service;

import com.codingShuttle.projects.buildX.platform.dto.subscription.CheckoutRequest;
import com.codingShuttle.projects.buildX.platform.dto.subscription.CheckoutResponse;
import com.codingShuttle.projects.buildX.platform.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);

    PortalResponse openCustomerPortal();

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
