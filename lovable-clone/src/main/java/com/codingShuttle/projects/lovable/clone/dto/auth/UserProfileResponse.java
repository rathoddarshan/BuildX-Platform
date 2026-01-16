package com.codingShuttle.projects.lovable.clone.dto.auth;

public record UserProfileResponse(
        Long id,
        String email,
        String name,
        String avatarUrl
) {

}
