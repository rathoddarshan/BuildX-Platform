package com.codingShuttle.projects.buildX.platform.repository;

import com.codingShuttle.projects.buildX.platform.entity.ChatSession;
import com.codingShuttle.projects.buildX.platform.entity.ChatSessonId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessonId> {
}
