package com.codingShuttle.projects.buildX.platform.repository;

import com.codingShuttle.projects.buildX.platform.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
}
