plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.8" // https://github.com/GradleUp/shadow
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
    api(project(":"))
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