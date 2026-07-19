package com.codingShuttle.projects.buildX.platform.controller;

import com.codingShuttle.projects.buildX.platform.dto.chat.ChatRequest;
import com.codingShuttle.projects.buildX.platform.dto.chat.ChatResponse;
import com.codingShuttle.projects.buildX.platform.dto.chat.StreamResponse;
import com.codingShuttle.projects.buildX.platform.service.AiGenerationService;
import com.codingShuttle.projects.buildX.platform.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final AiGenerationService aiGenerationService;
    private final ChatService chatService;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("@security.canEditProject(#request.projectId)")
    public SseEmitter streamChat(
            @RequestBody ChatRequest request
    ){
        SseEmitter emitter = new SseEmitter(10 * 60 * 1000L); // 10 minutes timeout

        aiGenerationService.streamResponse(request.message(), request.projectId())
                .subscribe(
                        data -> {
                            try {
                                emitter.send(SseEmitter.event().data(data));
                            } catch (Exception e) {
                                // client might have disconnected
                            }
                        },
                        error -> emitter.completeWithError(error),
                        emitter::complete
                );

        return emitter;
    }

    @GetMapping("/projects/{projectId}")
    @PreAuthorize("@security.canViewProject(#projectId)")
    public ResponseEntity<List<ChatResponse>> getChatHistory(
            @PathVariable Long projectId
    ){
        return ResponseEntity.ok(chatService.getProjectChatHistory(projectId));
    }
}
