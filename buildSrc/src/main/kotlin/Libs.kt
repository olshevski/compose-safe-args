object Libs {

    object Activity {
        const val Compose = "androidx.activity:activity-compose:1.4.0"
    }

    object Compose {
        @Suppress("MemberVisibilityCanBePrivate")
        const val Version = "1.1.1"
        const val Material = "androidx.compose.material:material:$Version"
        const val UiTooling = "androidx.compose.ui:ui-tooling:$Version"
        const val UiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$Version"
        const val UiTestJunit4 = "androidx.compose.ui:ui-test-junit4:$Version"
    }

    object Google {
        const val Truth = "com.google.truth:truth:1.1.3"
    }

    object JUnit {
        const val Juniper = "org.junit.jupiter:junit-jupiter:5.8.2"
    }

    object Lifecycle {
        private const val Version = "2.4.1"

        object ViewModel {
            const val Ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$Version"
            const val Compose = "androidx.lifecycle:lifecycle-viewmodel-compose:$Version"
            const val SavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$Version"
        }
    }

    object Lint {
        private const val Version = "30.1.3"
        const val Api = "com.android.tools.lint:lint-api:$Version"
        const val Checks = "com.android.tools.lint:lint-checks:$Version"
        const val Test = "com.android.tools.lint:lint-tests:$Version"
    }

    object Navigation {
        private const val Version = "2.4.2"
        const val Runtime = "androidx.navigation:navigation-runtime:$Version"
        const val Compose = "androidx.navigation:navigation-compose:$Version"
    }

    object Kotlin {
        const val Stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Plugins.Kotlin.Version}"
    }

    object Kotest {
        private const val Version = "5.2.2"
        const val RunnerJunit5 = "io.kotest:kotest-runner-junit5:$Version"
        const val FrameworkDataset = "io.kotest:kotest-framework-datatest:$Version"
    }

    object KotlinPoet {
        const val Ksp = "com.squareup:kotlinpoet-ksp:1.11.0"
    }

    object Ksp {
        val SymbolProcessingApi =
            "com.google.devtools.ksp:symbol-processing-api:${Plugins.Ksp.version}"
    }

    object Test {
        const val Core = "androidx.test:core:1.4.0"
    }

}