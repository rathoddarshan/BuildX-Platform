package com.codingShuttle.projects.lovable.clone.service;

import com.codingShuttle.projects.lovable.clone.dto.auth.AuthResponse;
import com.codingShuttle.projects.lovable.clone.dto.auth.LoginRequest;
import com.codingShuttle.projects.lovable.clone.dto.auth.SignupRequest;
import org.jspecify.annotations.Nullable;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
