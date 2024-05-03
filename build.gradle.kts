plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1" // https://github.com/johnrengelman/shadow
    `java-library`
    `maven-publish`
}

group = "com.andrew121410"
version = "1.0"
description = "CCUtils"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

tasks {
    build {
        dependsOn("shadowJar")
        dependsOn("processResources")
    }

    jar {
        enabled = false
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        // Not needed because it's a library.
        archiveFileName.set("CCUtilsJava.jar")

        relocate("com.google", "com.andrew121410.ccutils.dependencies.google")
        relocate("com.mysql", "com.andrew121410.ccutils.dependencies.mysql")
        relocate("org.xerial", "com.andrew121410.ccutils.dependencies.sqlite")
    }
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    api(libs.com.mysql.mysql.connector.j)
    api(libs.org.xerial.sqlite.jdbc)
    api(libs.com.google.guava.guava)
    compileOnly(libs.org.projectlombok.lombok)
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            artifact(tasks.named("shadowJar"))
        }
    }
}
