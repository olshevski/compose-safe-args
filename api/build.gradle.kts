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

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    api(Deps.Navigation.Runtime)
    testImplementation(Deps.Kotest.RunnerJunit5)
}