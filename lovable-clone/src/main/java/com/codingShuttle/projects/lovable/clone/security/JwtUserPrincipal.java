package com.codingShuttle.projects.lovable.clone.security;

public record JwtUserPrincipal(
        Long userId,
        String username
) {
}
