package com.codingShuttle.projects.buildX.platform.service.impl;

import com.codingShuttle.projects.buildX.platform.dto.subscription.PlanLimitResponse;
import com.codingShuttle.projects.buildX.platform.dto.subscription.PlanResponse;
import com.codingShuttle.projects.buildX.platform.dto.subscription.SubscriptionResponse;
import com.codingShuttle.projects.buildX.platform.dto.subscription.UsageTodayResponse;
import com.codingShuttle.projects.buildX.platform.entity.UsageLog;
import com.codingShuttle.projects.buildX.platform.repository.UsageLogRepository;
import com.codingShuttle.projects.buildX.platform.security.AuthUtil;
import com.codingShuttle.projects.buildX.platform.service.SubscriptionService;
import com.codingShuttle.projects.buildX.platform.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UsageServiceImpl implements UsageService {

    private final UsageLogRepository usageLogRepository;
    private final AuthUtil authUtil;
    private final SubscriptionService subscriptionService;

    @Override
    public void recordTokenUsage(Long userId, int actualToken) {
        LocalDate today = LocalDate.now();

        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId, today)
                .orElseGet(() -> createNewDailyLog(userId, today));

        todayLog.setTokensUsed(todayLog.getTokensUsed() + actualToken);
        usageLogRepository.save(todayLog);
    }


    @Override
    public void checkDailyTokenUsage() {

        Long userId = authUtil.getCurrentUserId();
        SubscriptionResponse subscriptionResponse = subscriptionService.getCurrentSubscription();
        PlanResponse plan = subscriptionResponse.plan();

        LocalDate today = LocalDate.now();
        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId, today)
                .orElseGet(() -> createNewDailyLog(userId, today));

        if (plan == null) {
            int currentUsage = todayLog.getTokensUsed();
            int freeLimit = 50000;
            if (currentUsage >= freeLimit) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                        "Daily free limit reached. Please upgrade your plan.");
            }
            return;
        }


        if(plan.unlimitedAi()) return;

        int currentUsage = todayLog.getTokensUsed();
        int limit = plan.maxToken();

        if(currentUsage >= limit){
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "daily limit reached, upgrade now");
        }


    }

    @Override
    public UsageTodayResponse getTodayUsageOfUser(Long userId) {
        LocalDate today = LocalDate.now();
        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId, today)
                .orElseGet(() -> createNewDailyLog(userId, today));

        SubscriptionResponse subscriptionResponse = subscriptionService.getCurrentSubscription();
        PlanResponse plan = subscriptionResponse.plan();

        int limit = plan != null ? plan.maxToken() : 0;

        return new UsageTodayResponse(
                todayLog.getTokensUsed(),
                limit,
                0, // previewsRunning (default 0 as not implemented in backend yet)
                0  // previewsLimit (default 0 as not implemented in backend yet)
        );
    }

    @Override
    public PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId) {
        SubscriptionResponse subscriptionResponse = subscriptionService.getCurrentSubscription();
        PlanResponse plan = subscriptionResponse.plan();

        if (plan == null) {
            return new PlanLimitResponse(
                    "Free Tier",
                    0,
                    100, // as defined by FREE_TIER_PROJECT_ALLOWED in SubscriptionServiceImpl
                    false
            );
        }

        return new PlanLimitResponse(
                plan.name(),
                plan.maxToken(),
                plan.maxProject(),
                plan.unlimitedAi()
        );
    }

    private UsageLog createNewDailyLog(Long userId, LocalDate date) {
        UsageLog newLog = UsageLog.builder()
                .userId(userId)
                .date(date)
                .tokensUsed(0)
                .build();

        return usageLogRepository.save(newLog);
    }

}
