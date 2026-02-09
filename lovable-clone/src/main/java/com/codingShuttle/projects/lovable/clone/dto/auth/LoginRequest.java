package com.codingShuttle.projects.lovable.clone.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Email String username,
        @Size(min = 4, max = 12) String password
) {
}
