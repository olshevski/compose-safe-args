plugins {
    plugin(Plugin.Android.Library)
    plugin(Plugin.Kotlin.Android)
    plugin(Plugin.Ksp)
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
    testImplementation(Lib.Kotest.RunnerJunit5)
    testImplementation(Lib.Kotest.FrameworkDataset)
}