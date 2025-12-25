package com.codingShuttle.projects.lovable.clone.entity;

import com.codingShuttle.projects.lovable.clone.enums.SubcriptionStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subcription {

    Long id;

    User user;

    Plan plan;

    String stripeCustomerId;
    String stripeSubcriptionId;

    SubcriptionStatus status;

    Instant currentPeriodStart;
    Instant currentPeriodEnd;
    Boolean cancelAtPeriodEnd;

    Instant createdAt;
    Instant updatedAt;
}
