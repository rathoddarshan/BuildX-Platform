package com.codingShuttle.projects.lovable.clone.repository;

import com.codingShuttle.projects.lovable.clone.entity.ProjectMember;
import com.codingShuttle.projects.lovable.clone.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId>{


    List<ProjectMember> findByIdProjectId(Long projectId);
}
