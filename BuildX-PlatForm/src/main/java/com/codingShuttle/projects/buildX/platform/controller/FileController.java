package com.codingShuttle.projects.buildX.platform.controller;

import com.codingShuttle.projects.buildX.platform.dto.project.FileContentResponse;
import com.codingShuttle.projects.buildX.platform.dto.project.FileNode;
import com.codingShuttle.projects.buildX.platform.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/{projectId}/files")
@RequiredArgsConstructor
public class FileController {

    private final ProjectFileService projectFileService;

    @GetMapping
    public ResponseEntity<List<FileNode>> getFileTree(@PathVariable Long projectId){
        Long userId = 1l;
        return ResponseEntity.ok(projectFileService.getFileTree(projectId));
    }

    @GetMapping("/content")
    public ResponseEntity<FileContentResponse> getFile(
            @PathVariable Long projectId,
            @RequestParam String path
    ){
        return ResponseEntity.ok(projectFileService.getFileContent(projectId, path));

    }
}
