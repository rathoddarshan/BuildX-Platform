package com.codingShuttle.projects.buildX.platform.service.impl;

import com.codingShuttle.projects.buildX.platform.dto.subscription.CheckoutRequest;
import com.codingShuttle.projects.buildX.platform.dto.subscription.CheckoutResponse;
import com.codingShuttle.projects.buildX.platform.dto.subscription.PortalResponse;
import com.codingShuttle.projects.buildX.platform.dto.subscription.SubscriptionResponse;
import com.codingShuttle.projects.buildX.platform.entity.Plan;
import com.codingShuttle.projects.buildX.platform.entity.Subscription;
import com.codingShuttle.projects.buildX.platform.entity.User;
import com.codingShuttle.projects.buildX.platform.enums.SubscriptionStatus;
import com.codingShuttle.projects.buildX.platform.error.ResourceNotFoundException;
import com.codingShuttle.projects.buildX.platform.mapper.SubscriptionMapper;
import com.codingShuttle.projects.buildX.platform.repository.PlanRepository;
import com.codingShuttle.projects.buildX.platform.repository.ProjectMemberRepository;
import com.codingShuttle.projects.buildX.platform.repository.SubscriptionRepository;
import com.codingShuttle.projects.buildX.platform.repository.UserRepository;
import com.codingShuttle.projects.buildX.platform.security.AuthUtil;
import com.codingShuttle.projects.buildX.platform.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final int FREE_TIER_PROJECT_ALLOWED = 1;

    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId = authUtil.getCurrentUserId();

        var currentSubscription =  subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(
                SubscriptionStatus.ACTIVE,
                SubscriptionStatus.PAST_DUE,
                SubscriptionStatus.TRIALING

        )).orElse(
                new Subscription()
        );

        return subscriptionMapper.toSubscriptionResponse(currentSubscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {

        boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if(exists) return;

        User user = getUser(userId);
        Plan plan = getPlan(planId);

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();

        subscriptionRepository.save(subscription);

    }

    @Override
    @Transactional
    public void updateSubscription(String gatewaySubscriptionId, SubscriptionStatus status,
                                   Instant periodStart, Instant periodEnd,
                                   Boolean cancelAtPeriodEnd, Long planId) {

        Subscription subscription = getSubscription(gatewaySubscriptionId);

        boolean subscriptionHasBeenUpdated = false;

        if(status == null && status != subscription.getStatus()){
            subscription.setStatus(status);
            subscriptionHasBeenUpdated = true;
        }

        if(periodStart != null && !periodStart.equals(subscription.getCurrentPeriodStart())){
            subscription.setCurrentPeriodStart(periodStart);
            subscriptionHasBeenUpdated = true;
        }

        if(periodEnd != null && !periodEnd.equals(subscription.getCurrentPeriodEnd())){
            subscription.setCurrentPeriodEnd(periodEnd);
            subscriptionHasBeenUpdated = true;
        }

        if(cancelAtPeriodEnd != null && cancelAtPeriodEnd != subscription.getCancelAtPeriodEnd()){
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            subscriptionHasBeenUpdated = true;
        }

        if (planId != null && !planId.equals(subscription.getPlan().getId())){
            Plan newPlan = getPlan(planId);
            subscription.setPlan(newPlan);
            subscriptionHasBeenUpdated = true;

        }

        if(subscriptionHasBeenUpdated){
            log.debug("Subscription has been updated {}", gatewaySubscriptionId);
            subscriptionRepository.save(subscription);
        }

    }

    @Override
    public void cancelSubscription(String gatewaySubscriptionId) {

        Subscription subscription = getSubscription(gatewaySubscriptionId);
        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);

    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {

        Subscription subscription = getSubscription(gatewaySubscriptionId);

        Instant newStart = periodStart != null ? periodStart : subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE || subscription.getStatus() == SubscriptionStatus.INCOMPLETE){
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

        subscriptionRepository.save(subscription);

    }


    @Override
    public void markSubscriptionPastDue(String gatewaySubscriptionId) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE){
            log.debug("Subscription is already PAST_DUE, gatewaySubscriptionId", gatewaySubscriptionId);
            return;
        }

        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);

    }


    @Override
    public boolean canCreateNewProject() {

        Long userId = authUtil.getCurrentUserId();

        int countOfOwnedProjects = projectMemberRepository.countProjectOwnedByUser(userId);

        SubscriptionResponse currentSubscription = getCurrentSubscription();
        if(currentSubscription.plan() == null){
            return countOfOwnedProjects < FREE_TIER_PROJECT_ALLOWED;
        }
        return countOfOwnedProjects < currentSubscription.plan().maxProject();
    }

    //utility methods

    private User getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User", userId.toString()));
    }

    private Plan getPlan(Long planId){
        return planRepository.findById(planId)
                .orElseThrow(()-> new ResourceNotFoundException("Plan", planId.toString()));
    }

    private Subscription getSubscription(String gatewaySubscriptionId) {
        return subscriptionRepository.findByStripeSubscriptionId(gatewaySubscriptionId).orElseThrow(() ->
                new ResourceNotFoundException("subscription", gatewaySubscriptionId));
    }


}
