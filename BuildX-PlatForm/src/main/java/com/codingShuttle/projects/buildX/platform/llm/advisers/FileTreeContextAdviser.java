package com.codingShuttle.projects.buildX.platform.llm.advisers;

import com.codingShuttle.projects.buildX.platform.dto.project.FileNode;
import com.codingShuttle.projects.buildX.platform.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileTreeContextAdviser implements StreamAdvisor {

    private final ProjectFileService projectFileService;

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain streamAdvisorChain) {

        Map<String, Object> context = request.context();
        Long projectId = Long.parseLong(context.getOrDefault("projectId", 0).toString());

        ChatClientRequest augmentedChatClientRequest = augmentedRequestWithFileTree(request, projectId);

        return streamAdvisorChain.nextStream(augmentedChatClientRequest);
    }

    private ChatClientRequest augmentedRequestWithFileTree(ChatClientRequest request, Long projectId) {

        List<Message> incomingMessages = request.prompt().getInstructions();

        Message systemMessage = incomingMessages.stream()
                .filter(m -> m.getMessageType() == MessageType.SYSTEM)
                .findFirst()
                .orElse(null);

        List<Message> userMessages = incomingMessages.stream()
                .filter(m -> m.getMessageType() != MessageType.SYSTEM)
                .toList();

        List<Message> allMessages = new ArrayList<>();

// Add original system message
        if (systemMessage != null) {
            allMessages.add(systemMessage);
        }

        List<FileNode> fileTree = projectFileService.getFileTree(projectId);
        String fileTreeContext = "\n\n ---- FILE_TREE \n"+fileTree.toString();
        userMessages.add(new SystemMessage(fileTreeContext));

        allMessages.addAll(userMessages);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
