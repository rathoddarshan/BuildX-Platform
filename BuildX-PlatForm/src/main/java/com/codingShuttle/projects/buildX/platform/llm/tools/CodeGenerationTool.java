package com.codingShuttle.projects.buildX.platform.llm.tools;


import com.codingShuttle.projects.buildX.platform.error.ToolBudgetExhaustedException;
import com.codingShuttle.projects.buildX.platform.service.ProjectFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

@Slf4j
public class CodeGenerationTool {

    private final ProjectFileService projectFileService;
    private final Long projectId;

    /**
     * Tracks how many distinct read_files INVOCATIONS have been made (not individual files).
     * This counts each time the LLM calls the tool, regardless of how many file paths it passes.
     */
    private int toolInvocationCount = 0;
    private static final int MAX_TOOL_INVOCATIONS = 10;

    /**
     * Cache of already-read file contents keyed by normalized path.
     * If the LLM asks for a file it already read, we return the cached content
     * without counting it as a new invocation.
     */
    private final Map<String, String> fileContentCache = new HashMap<>();

    public CodeGenerationTool(ProjectFileService projectFileService, Long projectId) {
        this.projectFileService = projectFileService;
        this.projectId = projectId;
    }

    @Tool(name = "read_files", description = "Read the content of files. Only the input file names present inside the FILE_TREE. Do not input any path which is not present under the FILE_TREE.")
    public List<String> readFiles(
            @ToolParam(description = "List of relative paths (e.g., ['src/app.tsx'])") List<String> paths) {

        // Check if ALL requested paths are already cached (pure re-read)
        List<String> normalizedPaths = paths.stream()
                .map(this::normalizePath)
                .toList();

        boolean allCached = normalizedPaths.stream().allMatch(fileContentCache::containsKey);

        if (!allCached) {
            toolInvocationCount++;
            log.info("Project {}: Tool invocation #{} for {} new file(s)", projectId, toolInvocationCount, normalizedPaths.size());
        } else {
            log.info("Project {}: Serving {} file(s) from cache (no budget cost)", projectId, normalizedPaths.size());
        }

        // If budget exceeded, return a summary of what was already read and instruct to proceed
        if (toolInvocationCount > MAX_TOOL_INVOCATIONS) {
            log.warn("Project {}: Tool invocation budget exhausted.", projectId);
            throw new ToolBudgetExhaustedException("File read budget exceeded.", projectId);
        }

        List<String> result = new ArrayList<>();
        for (String cleanPath : normalizedPaths) {
            // Return cached content if available
            if (fileContentCache.containsKey(cleanPath)) {
                result.add(fileContentCache.get(cleanPath));
                continue;
            }

            String content;
            try {
                content = projectFileService.getFileContent(projectId, cleanPath).content();
            } catch (Exception e) {
                content = "[File does not exist yet]";
            }

            String formatted = String.format(
                    "--- START OF FILE: %s ---\n%s\n--- END OF FILE---",
                    cleanPath, content);

            fileContentCache.put(cleanPath, formatted);
            result.add(formatted);
        }
        return result;
    }

    private String normalizePath(String path) {
        if (path == null) return "";
        String cleanPath = path.replace("\\", "/");
        if (cleanPath.startsWith("/")) {
            cleanPath = cleanPath.substring(1);
        }
        return cleanPath;
    }
}
