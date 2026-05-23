package com.codingShuttle.projects.buildX.platform.controller;

import com.codingShuttle.projects.buildX.platform.dto.chat.ChatRequest;
import com.codingShuttle.projects.buildX.platform.service.AiGenerationService;
import com.codingShuttle.projects.buildX.platform.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final AiGenerationService aiGenerationService;
    private final ChatService chatService;

    @PostMapping(value = "/api/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(
            @RequestBody ChatRequest request
    ){

        return aiGenerationService.streamResponse(request.message(), request.projectId())
                .map(data -> ServerSentEvent.<String>builder()
                        .data(data)
                        .build())
                .onErrorReturn(
                        ServerSentEvent.<String>builder()
                                        .event("error")
                                .data("Something went wrong, please try again.")
                                .build()
                );

    }
}
