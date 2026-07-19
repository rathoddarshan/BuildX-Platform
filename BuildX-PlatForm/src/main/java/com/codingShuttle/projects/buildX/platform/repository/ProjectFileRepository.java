package com.codingShuttle.projects.buildX.platform.repository;

import com.codingShuttle.projects.buildX.platform.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {
    <T> Optional<T> findByProjectIdAndPath(Long projectId, String cleanPath);

    List<ProjectFile> findByProjectId(Long projectId);

    @Query("SELECT pf FROM ProjectFile pf WHERE pf.project.id = :projectId AND (pf.isStarter = false OR pf.isStarter IS NULL)")
    List<ProjectFile> findNonStarterFilesByProjectId(@Param("projectId") Long projectId);
}
