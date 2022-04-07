plugins {
    plugin(Plugins.Kotlin.Jvm)
    plugin(Plugins.Android.Lint)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    compileOnly(Libs.Lint.Api)
    compileOnly(Libs.Lint.Checks)

    // Fixes "Found more than one jar in the 'lintPublish' configuration" error. Read more here:
    // https://pspdfkit.com/blog/2020/how-updating-to-kotlin-14-broke-our-linter-rules/
    compileOnly(Libs.Kotlin.Stdlib)

    testImplementation(Libs.JUnit.Juniper)
    testImplementation(Libs.Google.Truth)
    testImplementation(Libs.Lint.Api)
    testImplementation(Libs.Lint.Test)
}