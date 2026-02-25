package com.codingShuttle.projects.buildX.platform.service;

import com.codingShuttle.projects.buildX.platform.dto.project.ProjectRequest;
import com.codingShuttle.projects.buildX.platform.dto.project.ProjectResponse;
import com.codingShuttle.projects.buildX.platform.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {



    List<ProjectSummaryResponse> getUserProject(Long userId);

    ProjectResponse getUserProjectById(Long id, Long userId);

    ProjectResponse createProject(ProjectRequest request, Long userId);

    ProjectResponse updateProject(Long id, ProjectRequest request, Long userId);

    void softDelete(Long id, Long userId);
}
