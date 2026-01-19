package com.codingShuttle.projects.lovable.clone.repository;

import com.codingShuttle.projects.lovable.clone.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
