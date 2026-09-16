# Qwen Instructions

## Project

- This is a Maven Spring Boot application.
- The project targets Java 25.
- Use the Maven Wrapper for builds and tests.

## Commands

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-25.0.1.8-hotspot"
.\mvnw.cmd clean test
```

For compilation only:

```powershell
.\mvnw.cmd clean test-compile
```

## Guidelines

- Keep changes focused and preserve existing project conventions.
- Do not modify generated files under `target/`.
- Run the relevant Maven verification after code changes.
- Do not commit changes unless explicitly requested.
