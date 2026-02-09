package com.codingShuttle.projects.lovable.clone.mapper;

import com.codingShuttle.projects.lovable.clone.dto.auth.SignupRequest;
import com.codingShuttle.projects.lovable.clone.dto.auth.UserProfileResponse;
import com.codingShuttle.projects.lovable.clone.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignupRequest signupRequest);

    UserProfileResponse toUserProfileResponse(User user);
}
