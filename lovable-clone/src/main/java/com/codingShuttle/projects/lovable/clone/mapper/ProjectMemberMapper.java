package com.codingShuttle.projects.lovable.clone.mapper;

import com.codingShuttle.projects.lovable.clone.dto.member.MemberResponse;
import com.codingShuttle.projects.lovable.clone.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    MemberResponse toProjectMemberResponseFromOwner(User owner);
}
