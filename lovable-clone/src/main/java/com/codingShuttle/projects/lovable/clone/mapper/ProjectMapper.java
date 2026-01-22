package com.codingShuttle.projects.lovable.clone.mapper;

import com.codingShuttle.projects.lovable.clone.dto.project.ProjectResponse;
import com.codingShuttle.projects.lovable.clone.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);
}
