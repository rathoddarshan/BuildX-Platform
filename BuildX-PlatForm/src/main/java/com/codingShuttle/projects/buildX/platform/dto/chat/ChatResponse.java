package com.codingShuttle.projects.buildX.platform.dto.chat;

import com.codingShuttle.projects.buildX.platform.entity.ChatEvent;
import com.codingShuttle.projects.buildX.platform.entity.ChatSession;
import com.codingShuttle.projects.buildX.platform.enums.MessageRole;
import org.aspectj.bridge.Message;

import java.time.Instant;
import java.util.List;

public record ChatResponse(
        Long id,
        MessageRole messageRole,
        List<ChatEventResponse> events,
        String content,
        Integer tokensUsed,
        Instant created
) {
}
