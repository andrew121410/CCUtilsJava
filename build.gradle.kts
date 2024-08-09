plugins {
    id("io.freefair.lombok") version "8.7.1" // https://plugins.gradle.org/plugin/io.freefair.lombok
//    id("com.github.johnrengelman.shadow") version "8.1.1" // https://github.com/johnrengelman/shadow
    id("io.github.goooler.shadow") version "8.1.8" // https://github.com/johnrengelman/shadow/pull/876 https://github.com/Goooler/shadow https://plugins.gradle.org/plugin/io.github.goooler.shadow
    `java-library`
    `maven-publish`
}

group = "com.andrew121410"
version = "1.0"
description = "CCUtilsJava"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

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
        // Don't use just "archiveFileName.set("CCUtilsJava.jar")"
        archiveBaseName.set("CCUtilsJava")
        archiveClassifier.set("")
        archiveVersion.set("")
        // Jitpack.yml without the following 3 lines above, jitpack.io will not work.
        // It seems to produce a jar file like CCUtilsJava-12672e5a95-all.jar
        // Instead of CCUtilsJava-12672e5a95.jar notice the -all.
        // Maven or Gradle seems to try to find the jar file WITHOUT the -all.
        // So I wasted multiple hours trying to figure out why it wasn't working.

        relocate("com.google", "com.andrew121410.ccutils.dependencies.google")
        relocate("com.mysql", "com.andrew121410.ccutils.dependencies.mysql")
        relocate("org.xerial", "com.andrew121410.ccutils.dependencies.sqlite")
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api(libs.com.mysql.mysql.connector.j)
    api(libs.org.xerial.sqlite.jdbc)
    api(libs.com.google.guava.guava)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks["shadowJar"])
        }
    }
}