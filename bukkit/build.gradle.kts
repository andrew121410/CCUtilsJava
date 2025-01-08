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
//        archiveBaseName.set("CCUtilsJava")
//        archiveClassifier.set("Bukkit")
//        archiveVersion.set("")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks["shadowJar"])
        }
    }
}