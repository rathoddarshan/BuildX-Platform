package com.codingShuttle.projects.buildX.platform.dto.project;

import java.time.Instant;

public record FileNode(
        String path

) {
    @Override
    public String toString() {
        return path;
    }
}
