package com.codingShuttle.projects.lovable.clone.service.impl;

import com.codingShuttle.projects.lovable.clone.dto.auth.AuthResponse;
import com.codingShuttle.projects.lovable.clone.dto.auth.LoginRequest;
import com.codingShuttle.projects.lovable.clone.dto.auth.SignupRequest;
import com.codingShuttle.projects.lovable.clone.entity.User;
import com.codingShuttle.projects.lovable.clone.error.BadRequestException;
import com.codingShuttle.projects.lovable.clone.mapper.UserMapper;
import com.codingShuttle.projects.lovable.clone.repository.UserRepository;
import com.codingShuttle.projects.lovable.clone.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user ->{
            throw new BadRequestException("User Already exists with username: " + request.username());
        });

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));

        user = userRepository.save(user);

        return new AuthResponse("Dummy", userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
