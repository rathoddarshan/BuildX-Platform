package com.codingShuttle.projects.buildX.platform.service.impl;

import com.codingShuttle.projects.buildX.platform.dto.deploy.DeployResponse;
import com.codingShuttle.projects.buildX.platform.service.DeploymentService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KubernetesDeploymentServiceImpl implements DeploymentService {

    private final KubernetesClient client;

    private static final String NAMESPACE = "buildx-apps";
    private static final String POOL_LABEL = "status";
    private static final String PROJECT_LABEL = "project-id";
    private static final String IDLE = "idle";
    private static final String BUSY = "busy";
    private static final String SYNCER_CONTAINER = "syncer";
    private static final String RUNNER_CONTAINER = "runner";
    private static final String REVERSE_PROXY_PORT = "8090";

    @Override
    public DeployResponse deploy(Long projectId) {

        String domain = "project-" + projectId + ".app.domain.com";

        Pod existingPod = findActivePod(projectId);

        if (existingPod != null) {
            log.info("Project {} already has an active pod: {}", projectId, existingPod.getMetadata().getName());
            return new DeployResponse("http://" + domain + ":" + REVERSE_PROXY_PORT);
        }
        return claimAndStartNewPod(projectId, domain);
    }

    private DeployResponse claimAndStartNewPod(Long projectId, String domain) {

        Pod pod = client.pods()
                .inNamespace(NAMESPACE)
                .withLabel(POOL_LABEL, IDLE)
                .list()
                .getItems()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No idle runner available. Please scale up the runner pool."));

        String podName = pod.getMetadata().getName();

        log.info("Claiming pod {} for project {}", podName, projectId);

        client.pods()
                .inNamespace(NAMESPACE)
                .withName(podName)
                .edit(p -> {
                    if (p.getMetadata().getLabels() == null) {
                        p.getMetadata().setLabels(new java.util.HashMap<>());
                    }

                    p.getMetadata().getLabels().put(POOL_LABEL, BUSY);
                    p.getMetadata().getLabels().put(PROJECT_LABEL, projectId.toString());

                    return p;
                });

        try {

            // 1. Initial file sync from MinIO to /app (SYNCHRONOUS - waits for completion)
            String initialSyncCmd = String.format("""
                    set -e
                    rm -rf /app/*
                    mc mirror --overwrite myminio/projects/%d/ /app/
                    """, projectId);

            log.info("Executing initial sync for project {} on pod {}", projectId, podName);
            execCommand(podName, SYNCER_CONTAINER, "sh", "-c", initialSyncCmd);

            // 2. Start continuous sync in background (BACKGROUND - fully detached)
            String watchCmd = String.format("""
                    nohup mc mirror --overwrite --watch myminio/projects/%d/ /app/ \
                    > /app/sync.log 2>&1 </dev/null &
                    """, projectId);

            log.info("Starting background file watcher for project {} on pod {}", projectId, podName);
            execCommand(podName, SYNCER_CONTAINER, "sh", "-c", watchCmd);

            // 3. Install NPM dependencies (SYNCHRONOUS - waits up to 120s for npm install to finish)
            String installCmd = """
                    set -e
                    
                    cd /app
                    
                    while [ ! -f package.json ]
                    do
                      sleep 1
                    done
                    
                    npm install
                    """;

            log.info("Installing dependencies for project {} on pod {}", projectId, podName);
            execCommand(podName, RUNNER_CONTAINER, "sh", "-c", installCmd);

            // 4. Start the dev server in the background (BACKGROUND - fully detached)
            String startCmd = """
                    cd /app
                    
                    nohup npm run dev -- --host 0.0.0.0 --port 5173 \
                    > /app/dev.log 2>&1 </dev/null &
                    """;

            log.info("Starting dev server for project {} on pod {}", projectId, podName);
            execCommand(podName, RUNNER_CONTAINER, "sh", "-c", startCmd);

            log.info("Deployment completed for project {} on pod {}", projectId, podName);

            return new DeployResponse("http://" + domain + ":" + REVERSE_PROXY_PORT);

        }
        catch(Exception e){
            log.error("Deployment failed for project {}.", projectId, podName, e);
            client.pods().inNamespace(NAMESPACE).withName(podName).delete();
            throw new RuntimeException("Failed to deploy the project with id"+ projectId);
        }
    }

    private void execCommand(String podName, String container, String... command) {

        log.info("Executing in {}:{} -> {}", podName, container, String.join(" ", command));

        CompletableFuture<String> data = new CompletableFuture<>();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        try (ExecWatch ignored = client.pods()
                .inNamespace(NAMESPACE)
                .withName(podName)
                .inContainer(container)
                .writingOutput(out)
                .writingError(err)
                .usingListener(new ExecListener() {

                    @Override
                    public void onClose(int code, String reason) {
                        log.info("Exec finished with code {} : {}", code, reason);
                        data.complete("Done");
                    }
                })
                .exec(command)) {

            if (command[command.length - 1].trim().endsWith("&")) {
                Thread.sleep(1000);
            } else {
                data.get(120, TimeUnit.SECONDS); // Increased timeout to 120s for npm install
            }

            if (!out.toString().isBlank()) {
                log.info("STDOUT:\n{}", out);
            }

            if (!err.toString().isBlank()) {
                log.error("STDERR:\n{}", err);
            }

        } catch (Exception e) {
            log.error("Command failed.\nSTDOUT:\n{}\nSTDERR:\n{}",
                    out,
                    err,
                    e);

            throw new RuntimeException("Pod Execution Failed", e);
        }
    }

    Pod findActivePod(Long projectId) {
        return client.pods().inNamespace(NAMESPACE)
                .withLabel(PROJECT_LABEL, projectId.toString())
                .withLabel(POOL_LABEL, BUSY)
                .list().getItems().stream()
                .filter(pod -> pod.getStatus().getPhase().equals("Running"))
                .findFirst()
                .orElse(null);
    }
}