plugins {
    plugin(Plugins.Android.Application)
    plugin(Plugins.Kotlin.Android)
    plugin(Plugins.Ksp)
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
        kotlinCompilerExtensionVersion = Libs.Compose.Version
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
    implementation(project(":api-compose"))
    ksp(project(":ksp"))

    implementation(Libs.Compose.Material)
    debugImplementation(Libs.Compose.UiTooling)
    implementation(Libs.Compose.UiToolingPreview)
    implementation(Libs.Activity.Compose)
    implementation(Libs.Navigation.Compose)
    implementation(Libs.Lifecycle.ViewModel.Ktx)
    implementation(Libs.Lifecycle.ViewModel.Compose)
    implementation(Libs.Lifecycle.ViewModel.SavedState)

    androidTestImplementation(Libs.Test.Core)
    androidTestImplementation(Libs.Compose.UiTestJunit4)
}