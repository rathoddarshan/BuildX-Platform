package com.codingShuttle.projects.buildX.platform.security;

public record JwtUserPrincipal(
        Long userId,
        String username
) {
}
