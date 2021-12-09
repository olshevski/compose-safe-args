plugins {
    id("com.android.library")
    kotlin("android")
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

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    api(projects.api)
    api(Deps.Navigation.Compose)
}
