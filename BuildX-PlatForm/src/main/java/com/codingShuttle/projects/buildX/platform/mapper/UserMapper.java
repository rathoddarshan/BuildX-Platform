package com.codingShuttle.projects.buildX.platform.mapper;

import com.codingShuttle.projects.buildX.platform.dto.auth.SignupRequest;
import com.codingShuttle.projects.buildX.platform.dto.auth.UserProfileResponse;
import com.codingShuttle.projects.buildX.platform.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignupRequest signupRequest);

    UserProfileResponse toUserProfileResponse(User user);
}
