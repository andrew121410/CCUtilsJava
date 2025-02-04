plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "8.12.1" // https://plugins.gradle.org/plugin/io.freefair.lombok
}

group = "com.andrew121410"
version = "1.0"
description = "CCUtilsJava Core"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(libs.com.mysql.mysql.connector.j)
    compileOnly(libs.org.xerial.sqlite.jdbc)
    compileOnly(libs.com.google.guava.guava)
}

tasks {
    jar {
        enabled = true
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.andrew121410"
            artifactId = "ccutilsjava-core"
            version = "1.0"
            from(components["java"])
        }
    }
}
