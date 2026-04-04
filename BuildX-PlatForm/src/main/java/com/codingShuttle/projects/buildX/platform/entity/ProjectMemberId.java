package com.codingShuttle.projects.buildX.platform.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode  
public class ProjectMemberId {
    Long projectId;
    Long userId;
}
