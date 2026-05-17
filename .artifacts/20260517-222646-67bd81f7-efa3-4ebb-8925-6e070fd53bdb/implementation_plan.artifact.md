# Apply libs.versions.toml libraries and plugins

The goal is to ensure all relevant libraries and plugins defined in `libs.versions.toml` are correctly applied to the appropriate `build.gradle.kts` files in the project.

## Proposed Changes

### Root Project

#### [build.gradle.kts](file:///D:/projects/SevenStack/build.gradle.kts)
- Add missing plugins from `libs.versions.toml` with `apply false`: `ksp` and `room`.

### Core Module

#### [build.gradle.kts](file:///D:/projects/SevenStack/core/build.gradle.kts)
- Add common multiplatform libraries to `commonMain`:
    - `kotlinx-coroutines-core`
    - `kotlinx-serialization`
    - `kotlinx-datetime`
    - `room-runtime`
    - `androidx-sqlite-bundled`

### Server Module

#### [build.gradle.kts](file:///D:/projects/SevenStack/server/build.gradle.kts)
- Add database and serialization libraries:
    - `exposed-core`
    - `exposed-dao`
    - `exposed-kotlin-datetime`
    - `h2-database`
    - `kotlinx-serialization`
    - `kotlinx-coroutines-core`
    - `kotlinx-datetime`

### Shared App Module

#### [build.gradle.kts](file:///D:/projects/SevenStack/app/shared/build.gradle.kts)
- Add `voyager-navigator` for navigation if it's intended to be used.

## Verification Plan

### Automated Tests
- Run `./gradlew help` to ensure the build script configuration is valid and can be parsed.
- Run `./gradlew :server:assemble` to check if the server module builds with the new dependencies.
- Run `./gradlew :core:assemble` to check if the core module builds.
- Run `./gradlew :app:shared:assemble` to check if the shared app module builds.

### Manual Verification
- Check if the dependencies are correctly resolved in the IDE (simulated by checking if the task finishes successfully).
