package com.codingShuttle.projects.buildX.platform.service.impl;

import com.codingShuttle.projects.buildX.platform.dto.chat.ChatResponse;
import com.codingShuttle.projects.buildX.platform.entity.ChatMessage;
import com.codingShuttle.projects.buildX.platform.entity.ChatSession;
import com.codingShuttle.projects.buildX.platform.entity.ChatSessionId;
import com.codingShuttle.projects.buildX.platform.mapper.ChatMapper;
import com.codingShuttle.projects.buildX.platform.repository.ChatMessageRepository;
import com.codingShuttle.projects.buildX.platform.repository.ChatSessionRepository;
import com.codingShuttle.projects.buildX.platform.security.AuthUtil;
import com.codingShuttle.projects.buildX.platform.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatServiceImpl implements ChatService {

    ChatMessageRepository chatMessageRepository;
    AuthUtil authUtil;
    ChatSessionRepository chatSessionRepository;
    ChatMapper chatMapper;

    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {

        Long userId = authUtil.getCurrentUserId();

        ChatSession chatSession = chatSessionRepository.findById(
                new ChatSessionId(userId, projectId)
        ).orElseThrow(null);

        if(chatSession == null){
            return List.of();
        }

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatSession(chatSession);

        return chatMapper.fromListOfChatMessage(chatMessages);
    }
}
