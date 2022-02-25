import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    plugin(Plugin.Kotlin.Jvm)
    `publishing-config`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    implementation(Lib.Ksp.SymbolProcessingApi)
    implementation(Lib.KotlinPoet.Ksp) {
        exclude(module = "kotlin-reflect")
    }
}
