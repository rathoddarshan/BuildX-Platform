package com.codingShuttle.projects.buildX.platform.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.codingShuttle.projects.buildX.platform.enums.ProjectPermission.*;

@RequiredArgsConstructor
@Getter
public enum ProjectRole {
    EDITOR(VIEW, EDIT, DELETE, VIEW_MEMBERS),
    VIEWER(VIEW, VIEW_MEMBERS),
    OWNER(VIEW, EDIT, DELETE, MANAGE_MEMBERS, VIEW_MEMBERS);

    ProjectRole(ProjectPermission... perminssions){
        this.permissions = Set.of(perminssions);
    }

    private final Set<ProjectPermission> permissions;
}
