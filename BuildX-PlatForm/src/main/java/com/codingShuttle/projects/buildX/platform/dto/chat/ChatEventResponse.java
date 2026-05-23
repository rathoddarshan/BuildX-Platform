package com.codingShuttle.projects.buildX.platform.dto.chat;

import com.codingShuttle.projects.buildX.platform.enums.ChatEventType;

public record ChatEventResponse(
        Long id,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata
) {
}
