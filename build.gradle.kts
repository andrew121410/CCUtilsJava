import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("io.freefair.lombok") version "8.11"
    id("com.gradleup.shadow") version "8.3.5"
    `java-library`
    `maven-publish`
}

group = "com.andrew121410"
version = "1.0"
description = "CCUtilsJava"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api(libs.com.mysql.mysql.connector.j)
    api(libs.org.xerial.sqlite.jdbc)
    api(libs.com.google.guava.guava)
}

tasks {
    val baseJar = register<ShadowJar>("baseJar") {
        archiveBaseName.set("CCUtilsJavaBase")
        archiveClassifier.set("")
        archiveVersion.set("")
        from(sourceSets["main"].output)
        configurations = listOf(project.configurations.runtimeClasspath.get())
        exclude("META-INF/**")
    }

    val relocatedJar = register<ShadowJar>("relocatedJar") {
        archiveBaseName.set("CCUtilsJavaRelocated")
        archiveClassifier.set("")
        archiveVersion.set("")
        from(sourceSets["main"].output)
        configurations = listOf(project.configurations.runtimeClasspath.get())
        isEnableRelocation = true
        relocationPrefix = "com.andrew121410.ccutils.dependencies"
        exclude("META-INF/**")
    }

    val bukkitJar = register<ShadowJar>("bukkitJar") {
        archiveBaseName.set("CCUtilsJavaBukkit")
        archiveClassifier.set("")
        archiveVersion.set("")

        from(sourceSets["main"].output)
//        configurations = listOf(project.configurations.runtimeClasspath.get())

        // Remove the dependencies that are already in the Bukkit jar
//        dependencies {
//            exclude(dependency("com.mysql:mysql-connector-j"))
//            exclude(dependency("org.xerial:sqlite-jdbc"))
//            exclude(dependency("com.google.guava:guava"))
//        }

        exclude("META-INF/**")
    }

    build {
        dependsOn(baseJar, relocatedJar, bukkitJar)
    }

    jar {
        enabled = false
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}

publishing {
    publications {
        create<MavenPublication>("base") {
            artifact(tasks.named<ShadowJar>("baseJar").get())
        }
        create<MavenPublication>("relocated") {
            artifact(tasks.named<ShadowJar>("relocatedJar").get())
        }
        create<MavenPublication>("bukkit") {
            artifact(tasks.named<ShadowJar>("bukkitJar").get())
        }
    }
}
