plugins {
    plugin(Plugin.Android.Application)
    plugin(Plugin.Kotlin.Android)
    plugin(Plugin.Ksp)
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
        kotlinCompilerExtensionVersion = Lib.Compose.Version
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

    implementation(Lib.Compose.Material)
    debugImplementation(Lib.Compose.UiTooling)
    implementation(Lib.Compose.UiToolingPreview)
    implementation(Lib.Activity.Compose)
    implementation(Lib.Navigation.Compose)
    implementation(Lib.Lifecycle.ViewModel.Ktx)
    implementation(Lib.Lifecycle.ViewModel.Compose)
    implementation(Lib.Lifecycle.ViewModel.SavedState)

    androidTestImplementation(Lib.Test.Core)
    androidTestImplementation(Lib.Compose.UiTestJunit4)
}