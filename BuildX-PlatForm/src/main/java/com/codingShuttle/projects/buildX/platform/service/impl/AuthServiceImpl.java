package com.codingShuttle.projects.buildX.platform.service.impl;

import com.codingShuttle.projects.buildX.platform.dto.auth.AuthResponse;
import com.codingShuttle.projects.buildX.platform.dto.auth.LoginRequest;
import com.codingShuttle.projects.buildX.platform.dto.auth.SignupRequest;
import com.codingShuttle.projects.buildX.platform.entity.User;
import com.codingShuttle.projects.buildX.platform.error.BadRequestException;
import com.codingShuttle.projects.buildX.platform.mapper.UserMapper;
import com.codingShuttle.projects.buildX.platform.repository.UserRepository;
import com.codingShuttle.projects.buildX.platform.security.AuthUtil;
import com.codingShuttle.projects.buildX.platform.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthUtil authUtil;
    AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user ->{
            throw new BadRequestException("User Already exists with username: " + request.username());
        });

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));

        user = userRepository.save(user);

        String token = authUtil.generateAccessToken(user);

        return new AuthResponse(token, userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(user);
        return new AuthResponse(token, userMapper.toUserProfileResponse(user));
    }
}
