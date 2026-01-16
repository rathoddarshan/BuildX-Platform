package com.codingShuttle.projects.lovable.clone.entity;

import java.time.Instant;

public class UsageLog {
    Long id;
    User user;
    Project project;

    String action;

    Integer tokensUsed;
    Integer durationMs;

    String metaData;

    Instant createdAt;
}
