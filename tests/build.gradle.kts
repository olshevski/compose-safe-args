plugins {
    plugin(Plugins.Android.Library)
    plugin(Plugins.Kotlin.Android)
    plugin(Plugins.Ksp)
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

    libraryVariants.all {
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
    testImplementation(Libs.Kotest.RunnerJunit5)
    testImplementation(Libs.Kotest.FrameworkDataset)
}