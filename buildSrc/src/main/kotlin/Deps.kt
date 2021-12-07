@file:Suppress("SpellCheckingInspection")

object Deps {

    object Ksp {
        private const val Version = "1.5.31-1.0.1"
        const val SymbolProcessingApi = "com.google.devtools.ksp:symbol-processing-api:$Version"
    }

    object KotlinPoet {
        private const val Version = "1.10.2"
        const val Ksp = "com.squareup:kotlinpoet-ksp:$Version"
    }

    object Compose {
        @Suppress("MemberVisibilityCanBePrivate")
        const val Version = "1.0.5"
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
        private const val Version = "2.4.0-beta02"
        const val Runtime = "androidx.navigation:navigation-runtime:$Version"
        const val Compose = "androidx.navigation:navigation-compose:$Version"
    }

    object Kotest {
        private const val Version = "5.0.1"
        const val RunnerJunit5 = "io.kotest:kotest-runner-junit5:$Version"
        const val FrameworkDataset = "io.kotest:kotest-framework-datatest:$Version"
    }

    object Test {
        private const val Version = "1.4.0"
        const val Core = "androidx.test:core:$Version"
    }

}