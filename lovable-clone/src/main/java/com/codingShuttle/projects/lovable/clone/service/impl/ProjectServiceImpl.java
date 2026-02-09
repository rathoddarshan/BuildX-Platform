package com.codingShuttle.projects.lovable.clone.service.impl;

import com.codingShuttle.projects.lovable.clone.dto.project.ProjectRequest;
import com.codingShuttle.projects.lovable.clone.dto.project.ProjectResponse;
import com.codingShuttle.projects.lovable.clone.dto.project.ProjectSummaryResponse;
import com.codingShuttle.projects.lovable.clone.entity.Project;
import com.codingShuttle.projects.lovable.clone.entity.ProjectMember;
import com.codingShuttle.projects.lovable.clone.entity.ProjectMemberId;
import com.codingShuttle.projects.lovable.clone.entity.User;
import com.codingShuttle.projects.lovable.clone.enums.ProjectRole;
import com.codingShuttle.projects.lovable.clone.error.ResourceNotFoundException;
import com.codingShuttle.projects.lovable.clone.mapper.ProjectMapper;
import com.codingShuttle.projects.lovable.clone.repository.ProjectMemberRepository;
import com.codingShuttle.projects.lovable.clone.repository.ProjectRepository;
import com.codingShuttle.projects.lovable.clone.repository.UserRepository;
import com.codingShuttle.projects.lovable.clone.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {

        User owner = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User", userId.toString())
        );

        Project project = Project.builder()
                .name(request.name())
                .isPublic(false)
                .build();

        project = projectRepository.save(project);

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), owner.getId());

        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .projectRole(ProjectRole.OWNER)
                .user(owner)
                .acceptedAt(Instant.now())
                .invitedAt(Instant.now())
                .project(project)
                .build();

        projectMemberRepository.save(projectMember);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public List<ProjectSummaryResponse> getUserProject(Long userId) {

        var projects = projectRepository.findAllAccessibleByUser(userId);

        return projectMapper.toListOfProjectSummaryResponse(projects);
    }

    @Override
    public ProjectResponse getUserProjectById(Long id, Long userId) {

        Project project = getAccessibleProjectById(id, userId);
        return projectMapper.toProjectResponse(project);
    }


    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {

        Project project = getAccessibleProjectById(id, userId);

        project.setName(request.name());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id, Long userId) {

        Project project = getAccessibleProjectById(id, userId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);

    }

    public Project getAccessibleProjectById(Long projectId, Long userId){
        return projectRepository.findAccessibleProjectById(projectId, userId)
                .orElseThrow(()-> new ResourceNotFoundException("Project", projectId.toString())) ;
    }
}
