package com.codingShuttle.projects.buildX.platform.repository;

import com.codingShuttle.projects.buildX.platform.entity.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatEventRepository extends JpaRepository<ChatEvent, Long> {
}
