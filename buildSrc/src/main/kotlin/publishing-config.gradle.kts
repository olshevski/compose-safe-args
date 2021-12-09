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