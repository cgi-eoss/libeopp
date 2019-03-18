plugins {
    base
    id("com.github.zetten.bazel-dependencies-plugin") version "1.1.0"
    id("com.github.ben-manes.versions") version "0.21.0"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

val generate by configurations.creating

bazelDependencies {
    configuration = generate
    outputFile = project.rootDir.resolve("java_repositories.bzl")
    strictLicenses = true
    licenseOverrides = mapOf()
}

repositories {
    jcenter()
}

extra.set("guava.version", "27.1-jre")
extra.set("junit.version", "4.12")
extra.set("truth.version", "0.43")

dependencyManagement {
    dependencies {
        dependency("com.google.guava:guava:${extra.get("guava.version")}")
        dependency("com.google.truth.extensions:truth-java8-extension:${extra.get("truth.version")}")
        dependency("com.google.truth.extensions:truth-proto-extension:${extra.get("truth.version")}")
        dependency("com.google.truth:truth:${extra.get("truth.version")}")
        dependency("junit:junit:${extra.get("junit.version")}")
    }
}

dependencies {
    generate("com.google.guava:guava")
    generate("com.google.truth.extensions:truth-java8-extension")
    generate("com.google.truth.extensions:truth-proto-extension")
    generate("com.google.truth:truth")
    generate("junit:junit")
}
