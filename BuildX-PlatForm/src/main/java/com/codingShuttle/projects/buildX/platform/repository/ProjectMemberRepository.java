package com.codingShuttle.projects.buildX.platform.repository;

import com.codingShuttle.projects.buildX.platform.entity.ProjectMember;
import com.codingShuttle.projects.buildX.platform.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId>{


    List<ProjectMember> findByIdProjectId(Long projectId);
}
