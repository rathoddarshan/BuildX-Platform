package com.codingShuttle.projects.buildX.platform.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatSessonId implements Serializable {

    Long projectId;
    Long userId;
}
