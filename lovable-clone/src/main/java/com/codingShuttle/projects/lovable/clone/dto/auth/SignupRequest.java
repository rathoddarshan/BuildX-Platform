package com.codingShuttle.projects.lovable.clone.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank @Email String email,
        @Size(min = 1, max = 30) String name,
        @NotBlank @Size(min = 4, max = 12) String password
) {

}
