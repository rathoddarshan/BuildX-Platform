package com.codingShuttle.projects.buildX.platform.service;

import com.codingShuttle.projects.buildX.platform.dto.project.ProjectRequest;
import com.codingShuttle.projects.buildX.platform.dto.project.ProjectResponse;
import com.codingShuttle.projects.buildX.platform.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {



    List<ProjectSummaryResponse> getUserProject();

    ProjectResponse getUserProjectById(Long id);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void softDelete(Long id);
}
