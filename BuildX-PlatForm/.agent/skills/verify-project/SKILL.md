---
name: verify-buildx-project
description: Use this skill to run builds, tests, or compile checks specifically for the BuildX-PlatForm Java Maven project.
---

# Verify BuildX-PlatForm Maven Project

This skill teaches the agent how to build, test, and compile the BuildX-PlatForm project on Windows.

## Use this skill when
- Checking if Java/Spring Boot code changes compile successfully.
- Running unit or integration tests for the project.
- Packaging the project or executing Maven build commands.

## Do not use this skill when
- The task is purely research or non-Java (e.g., editing documentation).
- Working on a different project that does not use Maven or Spring Boot.

## Instructions
1. Since the host OS is Windows, always use the Maven wrapper script `.\mvnw.cmd` located inside the project root directory. Do not use global `mvn` commands.
2. To compile the project and check for syntax or type errors:
   ```cmd
   .\mvnw.cmd clean compile
   ```
3. To run the test suite:
   ```cmd
   .\mvnw.cmd test
   ```
4. If a test fails, analyze the test reports in `target/surefire-reports` to locate the source file and line of the failure.

## Security
- Do not run Maven goals that download arbitrary plugins from untrusted sources.
- Ensure the database or external services required for tests are running locally, or use test profiles.
