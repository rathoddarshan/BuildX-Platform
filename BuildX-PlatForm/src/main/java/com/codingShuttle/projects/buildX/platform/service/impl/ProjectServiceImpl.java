package com.codingShuttle.projects.buildX.platform.service.impl;

import com.codingShuttle.projects.buildX.platform.dto.project.ProjectRequest;
import com.codingShuttle.projects.buildX.platform.dto.project.ProjectResponse;
import com.codingShuttle.projects.buildX.platform.dto.project.ProjectSummaryResponse;
import com.codingShuttle.projects.buildX.platform.entity.Project;
import com.codingShuttle.projects.buildX.platform.entity.ProjectMember;
import com.codingShuttle.projects.buildX.platform.entity.ProjectMemberId;
import com.codingShuttle.projects.buildX.platform.entity.User;
import com.codingShuttle.projects.buildX.platform.enums.ProjectRole;
import com.codingShuttle.projects.buildX.platform.error.ResourceNotFoundException;
import com.codingShuttle.projects.buildX.platform.mapper.ProjectMapper;
import com.codingShuttle.projects.buildX.platform.repository.ProjectMemberRepository;
import com.codingShuttle.projects.buildX.platform.repository.ProjectRepository;
import com.codingShuttle.projects.buildX.platform.repository.UserRepository;
import com.codingShuttle.projects.buildX.platform.security.AuthUtil;
import com.codingShuttle.projects.buildX.platform.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;
    AuthUtil authUtil;

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        Long userId = authUtil.getCurrentUserId();
//        User owner = userRepository.findById(userId).orElseThrow(
//                () -> new ResourceNotFoundException("User", userId.toString())
//        );
        User owner = userRepository.getReferenceById(userId);

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
    public List<ProjectSummaryResponse> getUserProject() {
        Long userId = authUtil.getCurrentUserId();

        var projects = projectRepository.findAllAccessibleByUser(userId);

        return projectMapper.toListOfProjectSummaryResponse(projects);
    }

    @Override
    @PreAuthorize("@security.canViewProject(#projectId)")
    public ProjectResponse getUserProjectById(Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId, userId);
        return projectMapper.toProjectResponse(project);
    }


    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(id, userId);

        project.setName(request.name());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canDeleteProject(#projectId)")
    public void softDelete(Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId, userId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);

    }

    public Project getAccessibleProjectById(Long projectId, Long userId){
        return projectRepository.findAccessibleProjectById(projectId, userId)
                .orElseThrow(()-> new ResourceNotFoundException("Project", projectId.toString())) ;
    }
}
