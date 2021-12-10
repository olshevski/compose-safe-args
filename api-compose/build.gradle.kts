import com.android.build.gradle.internal.api.DefaultAndroidSourceDirectorySet

plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka")
    `publishing-config`
}

android {
    compileSdk = AndroidSdkVersion.Compile

    defaultConfig {
        minSdk = AndroidSdkVersion.Min
        targetSdk = AndroidSdkVersion.Target
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Deps.Compose.Version
    }
}

dependencies {
    api(projects.api)
    api(Deps.Navigation.Compose)
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

val sourcesJar by tasks.registering(Jar::class) {
    from((android.sourceSets["main"].kotlin as DefaultAndroidSourceDirectorySet).srcDirs)
    archiveClassifier.set("sources")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("apiCompose") {
                from(components["release"])
                artifact(sourcesJar)
                artifact(javadocJar)
            }
        }
    }
}