# CCUtils Library

CCUtils is a utility library for Java projects, providing various helper classes and methods for file operations, time
manipulation, SQL database interactions, and more.

## Features

- **File Utilities**: Download files, get the folder of the running JAR, etc.
- **Time Utilities**: Convert time durations into human-readable formats.
- **SQL Utilities**: Simplified interactions with MySQL and SQLite databases.
- **Resource Utilities**: Read resources from JAR files.
- **Hash-Based Updater**: Update JAR files based on hash comparison.

## Installation

### Gradle

1. Add the JitPack repository to your `build.gradle.kts`:

    ```kotlin
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
    ```

2. Add the CCUtils dependency:

    ```kotlin
    dependencies {
        implementation("com.github.andrew121410:CCUtilsJava:Tag")
    }
    ```

To find the latest version, visit [JitPack](https://jitpack.io/#andrew121410/CCUtilsJava/).