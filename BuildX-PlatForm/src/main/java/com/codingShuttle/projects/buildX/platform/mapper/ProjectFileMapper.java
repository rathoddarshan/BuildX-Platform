package com.codingShuttle.projects.buildX.platform.mapper;

import com.codingShuttle.projects.buildX.platform.dto.project.FileNode;
import com.codingShuttle.projects.buildX.platform.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);
}