package com.codingShuttle.projects.buildX.platform.service;

import com.codingShuttle.projects.buildX.platform.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {

    List<ChatResponse> getProjectChatHistory(Long projectId);
}
