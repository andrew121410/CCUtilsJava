plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.5" // https://github.com/GradleUp/shadow
}

group = "com.andrew121410"
version = "1.0"
description = "CCUtilsJava Bukkit"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api(project(":core"))
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    jar {
        enabled = false
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveBaseName.set("CCUtilsJava")
        archiveClassifier.set("")
        archiveVersion.set("")

        configurations = listOf(
            project.configurations.compileClasspath.get().exclude(group = "com.mysql", module = "mysql-connector-java"),
            project.configurations.compileClasspath.get().exclude(group = "org.xerial", module = "sqlite-jdbc"),
            project.configurations.compileClasspath.get().exclude(group = "com.google.guava", module = "guava")
        )
        exclude("com/mysql/**")
        exclude("org/xerial/**")
        exclude("com/google/**")
        exclude("google/**")

        // Random ahh stuff
        exclude("README")
        exclude("LICENSE")
        exclude("INFO_SRC")
        exclude("INFO_BIN")

        exclude("META-INF/**")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.andrew121410"
            artifactId = "ccutilsjava-bukkit"
            version = "1.0"
            artifact(tasks["shadowJar"])
        }
    }
}