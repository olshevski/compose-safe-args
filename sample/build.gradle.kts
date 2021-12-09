plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = AndroidSdkVersion.Compile

    defaultConfig {
        applicationId = "dev.olshevski.safeargs.sample"
        minSdk = AndroidSdkVersion.Min
        targetSdk = AndroidSdkVersion.Target
        versionName = project.property("version").toString()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    applicationVariants.all {
        val variantName = name
        sourceSets {
            named(variantName) {
                kotlin.srcDir(file("build/generated/ksp/$variantName/kotlin"))
            }
        }
    }
}

dependencies {
    implementation(projects.apiCompose)
    ksp(projects.ksp)

    implementation(Deps.Compose.Material)
    debugImplementation(Deps.Compose.UiTooling)
    implementation(Deps.Compose.UiToolingPreview)
    implementation(Deps.Activity.Compose)
    implementation(Deps.Navigation.Compose)
    implementation(Deps.Lifecycle.ViewModel.Ktx)
    implementation(Deps.Lifecycle.ViewModel.Compose)
    implementation(Deps.Lifecycle.ViewModel.SavedState)

    androidTestImplementation(Deps.Test.Core)
    androidTestImplementation(Deps.Compose.UiTestJunit4)
}