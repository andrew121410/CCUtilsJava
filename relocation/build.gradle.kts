plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.1.0" // https://github.com/GradleUp/shadow
}

group = "com.andrew121410"
version = "1.0"
description = "CCUtilsJava Relocation"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    mavenLocal()
}
dependencies {
    api(project(":bundled"))
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

        relocate("com.google", "com.andrew121410.ccutils.dependencies.google")
        relocate("com.mysql", "com.andrew121410.ccutils.dependencies.mysql")
        relocate("org.xerial", "com.andrew121410.ccutils.dependencies.sqlite")

        // When I use this it breaks SQLite.
        // java.lang.UnsatisfiedLinkError:
        // 'void com.andrew121410.mc.world16utils.utils.ccutils.dependencies.org.sqlite.core.NativeDB._open_utf8(byte[], int)'
//        isEnableRelocation = true
//        relocationPrefix = "com.andrew121410.ccutils.dependencies"
//        exclude("META-INF/**")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.andrew121410"
            artifactId = "ccutilsjava-relocation"
            version = "1.0"
            artifact(tasks["shadowJar"])
        }
    }
}
