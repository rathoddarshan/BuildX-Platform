package com.codingShuttle.projects.lovable.clone.dto.subscription;

public record UsageTodayResponse(
        int tokensUsed,
        int tokensLimit,
        int previewsRunning,
        int previewsLimit

) {
}
