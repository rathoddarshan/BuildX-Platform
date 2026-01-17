package com.codingShuttle.projects.lovable.clone.controller;

import com.codingShuttle.projects.lovable.clone.dto.subscription.*;
import com.codingShuttle.projects.lovable.clone.service.PlanService;
import com.codingShuttle.projects.lovable.clone.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/api/plans")
    public ResponseEntity<PlanResponse> getAllPlan(){
        return ResponseEntity.ok(planService.getAllActivePlan());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription(){
        Long userId = 1l;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping("/api/stripe/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(
            @RequestBody CheckoutRequest request
    ) {
        Long userId = 1l;
        return ResponseEntity.ok(subscriptionService.createCheckoutSessionUrl(request, userId));
    }


    @PostMapping("/api/stripe/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal(){
        Long userId = 1l;
        return ResponseEntity.ok(subscriptionService.openCustomerPortal(userId));

    }
}
