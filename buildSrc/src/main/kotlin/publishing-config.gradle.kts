plugins {
    `maven-publish`
    signing
}

fun createPublicationName() = name.split("-")
    .mapIndexed { index, s ->
        if (index == 0) s else s.capitalize()
    }
    .joinToString(separator = "")

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>(createPublicationName()) {
                from(components.findByName("release") ?: components.findByName("java")!!)

                pom {
                    name.set("Compose Safe Args")
                    description.set("Missing safe arguments generator for Android Compose Navigation")
                    url.set("https://github.com/olshevski/compose-safe-args")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://github.com/olshevski/compose-safe-args/blob/main/LICENSE")
                        }
                    }

                    developers {
                        developer {
                            id.set("olshevski")
                            name.set("Vitali Olshevski")
                            email.set("tech@olshevski.dev")
                            url.set("https://olshevski.dev")
                        }
                    }

                    scm {
                        connection.set("scm:git:https://github.com/olshevski/compose-safe-args.git")
                        developerConnection.set("scm:git:https://github.com/olshevski/compose-safe-args.git")
                        url.set("https://github.com/olshevski/compose-safe-args")
                    }
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        project.properties["signing.key"].toString(),
        project.properties["signing.password"].toString(),
    )
    sign(publishing.publications)
}