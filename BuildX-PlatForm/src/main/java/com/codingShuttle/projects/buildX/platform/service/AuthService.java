package com.codingShuttle.projects.buildX.platform.service;

import com.codingShuttle.projects.buildX.platform.dto.auth.AuthResponse;
import com.codingShuttle.projects.buildX.platform.dto.auth.LoginRequest;
import com.codingShuttle.projects.buildX.platform.dto.auth.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
