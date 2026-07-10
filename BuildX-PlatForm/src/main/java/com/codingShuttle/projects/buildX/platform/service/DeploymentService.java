package com.codingShuttle.projects.buildX.platform.service;

import com.codingShuttle.projects.buildX.platform.dto.deploy.DeployResponse;

public interface DeploymentService {

    DeployResponse deploy(Long projectId);
}
