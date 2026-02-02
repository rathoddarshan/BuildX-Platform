package com.codingShuttle.projects.lovable.clone.controller;

import com.codingShuttle.projects.lovable.clone.dto.project.ProjectRequest;
import com.codingShuttle.projects.lovable.clone.dto.project.ProjectResponse;
import com.codingShuttle.projects.lovable.clone.dto.project.ProjectSummaryResponse;
import com.codingShuttle.projects.lovable.clone.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getMyProjects(){
        Long userId = 1l;//TODo:- will update later with real spring security.
        return ResponseEntity.ok(projectService.getUserProject(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id){
        Long userId = 1l;
        return ResponseEntity.ok(projectService.getUserProjectById(id, userId));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest request){
        Long userId = 1l;
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request, userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectRequest request){
        Long userId = 1l;
        return ResponseEntity.ok(projectService.updateProject(id, request, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        Long userId = 1l;
        projectService.softDelete(id, userId);
        return ResponseEntity.noContent().build();
    }

}
