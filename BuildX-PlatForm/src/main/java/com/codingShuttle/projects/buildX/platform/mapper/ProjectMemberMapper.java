package com.codingShuttle.projects.buildX.platform.mapper;

import com.codingShuttle.projects.buildX.platform.dto.member.MemberResponse;
import com.codingShuttle.projects.buildX.platform.entity.ProjectMember;
import com.codingShuttle.projects.buildX.platform.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "projectRole", constant = "OWNER")
    MemberResponse toProjectMemberResponseFromOwner(User owner);


    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "name", source = "user.name")
    MemberResponse toProjectMemberResponseFromMember(ProjectMember projectMember);
}
