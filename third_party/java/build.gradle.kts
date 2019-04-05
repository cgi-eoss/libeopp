plugins {
    base
    id("com.github.zetten.bazel-dependencies-plugin") version "1.4.0"
    id("com.github.ben-manes.versions") version "0.21.0"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

val generate by configurations.creating

bazelDependencies {
    configuration = generate
    outputFile = project.rootDir.resolve("java_repositories.bzl")
    strictLicenses = true
    safeSources = true
    sourcesChecksums = true
    licenseOverrides = mapOf()
}

repositories {
    jcenter()
}

extra.set("google-common-protos.version", "1.15.0")
extra.set("grpc.version", "1.19.0")
extra.set("guava.version", "27.1-jre")
extra.set("spring-boot.version", "2.1.4.RELEASE")
extra.set("spring-cloud.version", "Greenwich.SR1")
extra.set("truth.version", "0.44")

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${extra.get("spring-cloud.version")}")
        mavenBom("org.springframework.boot:spring-boot-dependencies:${extra.get("spring-boot.version")}")
    }
    dependencies {
        dependency("com.google.api.grpc:proto-google-common-protos:${extra.get("google-common-protos.version")}")
        dependency("com.google.guava:guava:${extra.get("guava.version")}")
        dependency("com.google.truth.extensions:truth-java8-extension:${extra.get("truth.version")}")
        dependency("com.google.truth.extensions:truth-proto-extension:${extra.get("truth.version")}")
        dependency("com.google.truth:truth:${extra.get("truth.version")}")
        dependency("io.grpc:grpc-core:${extra.get("grpc.version")}")
        dependency("io.grpc:grpc-stub:${extra.get("grpc.version")}")
    }
}

dependencies {
    generate("com.google.api.grpc:proto-google-common-protos")
    generate("com.google.guava:guava")
    generate("com.google.truth.extensions:truth-java8-extension")
    generate("com.google.truth.extensions:truth-proto-extension")
    generate("com.google.truth:truth")
    generate("io.grpc:grpc-core")
    generate("io.grpc:grpc-stub")
    generate("javax.annotation:javax.annotation-api")
    generate("junit:junit")
    generate("org.mockito:mockito-core")
    generate("org.slf4j:slf4j-api")
    generate("org.springframework.cloud:spring-cloud-commons")
    generate("org.springframework:spring-core")
}
