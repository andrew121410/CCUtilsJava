plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "9.2.0" // https://plugins.gradle.org/plugin/io.freefair.lombok
    id("com.gradleup.shadow") version "9.3.1" // https://github.com/GradleUp/shadow
}

group = "com.andrew121410"
version = "1.0"
description = "CCUtilsJava Bundled"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api(project(":"))

    api(libs.com.mysql.mysql.connector.j)
    api(libs.org.xerial.sqlite.jdbc)
    api(libs.com.google.guava.guava)
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    jar {
        enabled = true
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        // Don't use just "archiveFileName.set("CCUtilsJava.jar")"
        archiveBaseName.set("CCUtilsJava")
        archiveClassifier.set("")
        archiveVersion.set("")

        // Jitpack.yml without the following 3 lines above, jitpack.io will not work.
        // It seems to produce a jar file like CCUtilsJava-12672e5a95-all.jar
        // Instead of CCUtilsJava-12672e5a95.jar notice the -all.
        // Maven or Gradle seems to try to find the jar file WITHOUT the -all.
        // So I wasted multiple hours trying to figure out why it wasn't working.
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.andrew121410"
            artifactId = "ccutilsjava-bundled"
            version = "1.0"
            artifact(tasks["shadowJar"])
        }
    }
}
