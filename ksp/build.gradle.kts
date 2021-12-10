import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
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
    implementation(Deps.Ksp.SymbolProcessingApi)
    implementation(Deps.KotlinPoet.Ksp) {
        exclude(module = "kotlin-reflect")
    }
}

publishing {
    publications {
        create<MavenPublication>("ksp") {
            from(components["java"])
        }
    }
}