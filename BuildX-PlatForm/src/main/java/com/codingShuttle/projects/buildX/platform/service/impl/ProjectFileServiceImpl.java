package com.codingShuttle.projects.buildX.platform.service.impl;

import com.codingShuttle.projects.buildX.platform.dto.project.FileContentResponse;
import com.codingShuttle.projects.buildX.platform.dto.project.FileNode;
import com.codingShuttle.projects.buildX.platform.entity.Project;
import com.codingShuttle.projects.buildX.platform.entity.ProjectFile;
import com.codingShuttle.projects.buildX.platform.error.ResourceNotFoundException;
import com.codingShuttle.projects.buildX.platform.mapper.ProjectFileMapper;
import com.codingShuttle.projects.buildX.platform.repository.ProjectFileRepository;
import com.codingShuttle.projects.buildX.platform.repository.ProjectRepository;
import com.codingShuttle.projects.buildX.platform.service.ProjectFileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectFileServiceImpl implements ProjectFileService {

    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final MinioClient minioClient;
    private final ProjectFileMapper projectFileMapper;


    @Value("${minio.project-bucket}")
    private String projectBucket;

    private static final String BUCKET_NAME = "projects";

    @Override
    public List<FileNode> getFileTree(Long projectId) {

        List<ProjectFile> projectFileList = projectFileRepository.findNonStarterFilesByProjectId(projectId);
        return projectFileMapper.toListOfFileNode(projectFileList);

    }

    @Override
    public List<FileNode> getLlmFileTree(Long projectId) {

        List<ProjectFile> projectFileList = projectFileRepository.findByProjectId(projectId);
        return projectFileMapper.toListOfFileNode(projectFileList);

    }

    private String normalizePath(String path) {
        if (path == null) return "";
        String cleanPath = path.replace("\\", "/");
        if (cleanPath.startsWith("/")) {
            cleanPath = cleanPath.substring(1);
        }
        return cleanPath;
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path) {

        String cleanPath = normalizePath(path);
        String objectKey = projectId + "/" + cleanPath;

        try(
            InputStream is = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectKey)
                            .build())){

            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return new FileContentResponse(path, content);

        } catch (Exception e) {
            log.info("Failed to read file: {}/{}", projectId, path, e);
            throw new RuntimeException("Failed to read file Content", e);
        }
    }

    @Override
    public void saveFile(Long projectId, String path, String content) {
        log.info("we are saving the file: {}", path);
        //save the metadata in postgres
        //save the file content inside MinIO

        Project project = projectRepository.findById(projectId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Project", projectId.toString()));

        String cleanPath = normalizePath(path);
        String objectKey = projectId + "/" + cleanPath;

        try {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            InputStream inputStream = new ByteArrayInputStream(contentBytes);
            //saving the file in the minio
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(projectBucket)
                            .object(objectKey)
                            .stream(inputStream, contentBytes.length, -1)
                            .contentType(determineContentType(path))
                            .build()
            );
            //saving the file metadata in the db
            ProjectFile file = (ProjectFile) projectFileRepository.findByProjectIdAndPath(projectId, cleanPath)
                    .orElseGet(() -> ProjectFile.builder()
                            .project(project)
                            .path(cleanPath)
                            .minioObjectKey(objectKey)
                            .createdAt(Instant.now())
                            .build()
                    );

            file.setUpdatedAt(Instant.now());
            file.setIsStarter(false);
            projectFileRepository.save(file);

            log.info("Saved file {}", objectKey);

        } catch (Exception e) {
            log.error("failed to save file {}/{}", projectId, cleanPath, e);
            throw new RuntimeException("File save failed", e);
        }

    }
    private String determineContentType(String path) {
        String type = URLConnection.guessContentTypeFromName(path);
        if (type != null) return type;

        if (path.endsWith(".jsx") || path.endsWith(".ts") || path.endsWith(".tsx"))
            return "text/javascript";

        if (path.endsWith(".json"))
            return "application/json";

        if (path.endsWith(".css"))
            return "text/css";

        return "text/plain";
    }
}
