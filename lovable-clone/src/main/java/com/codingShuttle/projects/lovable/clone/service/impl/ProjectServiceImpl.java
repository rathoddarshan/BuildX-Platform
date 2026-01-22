package com.codingShuttle.projects.lovable.clone.service.impl;

import com.codingShuttle.projects.lovable.clone.dto.project.ProjectRequest;
import com.codingShuttle.projects.lovable.clone.dto.project.ProjectResponse;
import com.codingShuttle.projects.lovable.clone.dto.project.ProjectSummaryResponse;
import com.codingShuttle.projects.lovable.clone.entity.Project;
import com.codingShuttle.projects.lovable.clone.entity.User;
import com.codingShuttle.projects.lovable.clone.mapper.ProjectMapper;
import com.codingShuttle.projects.lovable.clone.repository.ProjectRepository;
import com.codingShuttle.projects.lovable.clone.repository.UserRepository;
import com.codingShuttle.projects.lovable.clone.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {

        User owner = userRepository.findById(userId).orElseThrow();

        Project project = Project.builder()
                .name(request.name())
                .owner(owner)
                .build();

        project = projectRepository.save(project);

        return projectMapper.toProjectResponse(project);
    }

    @Override
    public List<ProjectSummaryResponse> getUserProject(Long userId) {
        return List.of();
    }

    @Override
    public ProjectResponse getUserProjectById(Long id, Long userId) {
        return null;
    }


    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        return null;
    }

    @Override
    public void softDelete(Long id, Long userId) {

    }
}
