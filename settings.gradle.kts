enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        val androidPluginVersion = "7.1.0-beta04"
        id("com.android.application") version androidPluginVersion
        id("com.android.library") version androidPluginVersion

        val kotlinPluginVersion: String = "1.5.31"
        kotlin("android") version kotlinPluginVersion
        kotlin("jvm") version kotlinPluginVersion

        id("com.google.devtools.ksp") version "1.5.31-1.0.1"
        id("com.github.ben-manes.versions") version "0.39.0"
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "compose-safe-args"
include(":api")
include(":api-compose")
include(":ksp")
include(":sample")
include(":tests")
