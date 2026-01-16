package com.codingShuttle.projects.lovable.clone.entity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.logging.Level;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class User {

    private Long id;

    private String email;
    private String passwordHash;
    private String name;

    private String avatarUrl;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

}
