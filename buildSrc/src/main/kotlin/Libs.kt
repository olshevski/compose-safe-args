object Libs {

    object Ksp {
        const val SymbolProcessingApi = "com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.4"
    }

    object KotlinPoet {
        const val Ksp = "com.squareup:kotlinpoet-ksp:1.10.2"
    }

    object Compose {
        @Suppress("MemberVisibilityCanBePrivate")
        const val Version = "1.1.1"
        const val Material = "androidx.compose.material:material:$Version"
        const val UiTooling = "androidx.compose.ui:ui-tooling:$Version"
        const val UiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$Version"
        const val UiTestJunit4 = "androidx.compose.ui:ui-test-junit4:$Version"
    }

    object Activity {
        const val Compose = "androidx.activity:activity-compose:1.4.0"
    }

    object Lifecycle {
        private const val Version = "2.4.0"

        object ViewModel {
            const val Ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$Version"
            const val Compose = "androidx.lifecycle:lifecycle-viewmodel-compose:$Version"
            const val SavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$Version"
        }
    }

    object Navigation {
        private const val Version = "2.4.1"
        const val Runtime = "androidx.navigation:navigation-runtime:$Version"
        const val Compose = "androidx.navigation:navigation-compose:$Version"
    }

    object Kotest {
        private const val Version = "5.1.0"
        const val RunnerJunit5 = "io.kotest:kotest-runner-junit5:$Version"
        const val FrameworkDataset = "io.kotest:kotest-framework-datatest:$Version"
    }

    object Test {
        const val Core = "androidx.test:core:1.4.0"
    }

}