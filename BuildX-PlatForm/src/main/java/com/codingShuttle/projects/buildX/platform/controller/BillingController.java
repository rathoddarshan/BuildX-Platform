package com.codingShuttle.projects.buildX.platform.controller;

import com.codingShuttle.projects.buildX.platform.dto.subscription.*;
import com.codingShuttle.projects.buildX.platform.service.PaymentProcessor;
import com.codingShuttle.projects.buildX.platform.service.PlanService;
import com.codingShuttle.projects.buildX.platform.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    private final PaymentProcessor paymentProcessor;

    @GetMapping("/api/plans")
    public ResponseEntity<PlanResponse> getAllPlan(){
        return ResponseEntity.ok(planService.getAllActivePlan());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription(){
        Long userId = 1l;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping("/api/payments/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(
            @RequestBody CheckoutRequest request
    ) {
        return ResponseEntity.ok(paymentProcessor.  createCheckoutSessionUrl(request));
    }


    @PostMapping("/api/payments/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal(){
        Long userId = 1l;
        return ResponseEntity.ok(paymentProcessor.openCustomerPortal(userId));

    }
}
