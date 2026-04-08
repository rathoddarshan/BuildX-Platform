package com.codingShuttle.projects.buildX.platform.service;

import reactor.core.publisher.Flux;

public interface AiGenerationService {
    Flux<String> streamResponse(String message, Long projectId);
}
