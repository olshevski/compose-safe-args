import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("io.github.gradle-nexus.publish-plugin")
    id("com.github.ben-manes.versions")
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        fun isStable(version: String): Boolean {
            val stableKeyword =
                listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
            return stableKeyword || "^[0-9,.v-]+(-r)?$".toRegex().matches(version)
        }
        isStable(currentVersion) && !isStable(candidate.version)
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}