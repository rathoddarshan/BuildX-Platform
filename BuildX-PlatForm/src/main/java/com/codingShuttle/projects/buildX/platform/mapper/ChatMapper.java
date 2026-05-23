package com.codingShuttle.projects.buildX.platform.mapper;

import com.codingShuttle.projects.buildX.platform.dto.chat.ChatResponse;
import com.codingShuttle.projects.buildX.platform.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    List<ChatResponse> fromListOfChatMessage(List<ChatMessage> chatMessages);
}
