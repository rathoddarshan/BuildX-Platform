package com.codingShuttle.projects.buildX.platform.llm.tools;


import com.codingShuttle.projects.buildX.platform.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class CodeGenerationTool {

    private final ProjectFileService projectFileService;
    private final Long projectId;

    @Tool(name = "read_files", description = "Read the content of files. Only the input file names present inside the FILE_TREE. Do not input any path. which is not present under the FILE_TREE.")
    public List<String> readFiles(
            @ToolParam(description = "List of relative paths (e.g., ['src/app.tsx'])") List<String> paths) {

        List<String> result = new ArrayList<>();

        for (String path : paths) {
            String cleanPath = path.startsWith("/") ? path.substring(1) : path;
            log.info("Requested File: {}", cleanPath);

            String content;
            try {
                content = projectFileService.getFileContent(projectId, cleanPath).content();
            } catch (Exception e) {
                // Return a descriptive placeholder so the LLM knows it needs to create/write the file
                content = "[File does not exist yet]";
            }

            result.add(String.format(
                    "--- START OF FILE: %s ---\n%s\n--- END OF FILE---",
                    cleanPath, content));
        }
        return result;
    }
}
