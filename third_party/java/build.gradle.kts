plugins {
    `java-base`
    id("com.github.zetten.bazel-dependencies-plugin") version "3.0.1"
    id("com.github.ben-manes.versions") version "0.52.0"
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r|jre)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

val generate: Configuration by configurations.creating

bazelDependencies {
    configuration = generate
    outputFile = project.layout.projectDirectory.file("java_repositories.bzl")
    rulesJvmExternal {
        version = "6.5"
    }
}

repositories {
    mavenCentral()
}

// BOMs
extra["aws-sdk-v2.version"] = "2.32.9"
extra["grpc-java.version"] = "1.73.0"
extra["kotlin.version"] = "2.1.21"
extra["kotlinx-coroutines.version"] = "1.10.2"
extra["okhttp.version"] = "5.1.0"
extra["opentelemetry.version"] = "1.52.0"
extra["protobuf-java.version"] = "4.31.1"
extra["spring-boot.version"] = "3.5.4"
extra["spring-cloud.version"] = "2025.0.0"

extra["azure-storage-blob.version"] = "12.31.0"
extra["commons-compress.version"] = "1.27.1"
extra["docker-java.version"] = "3.5.3"
extra["failsafe.version"] = "3.3.2"
extra["google-cloud-storage.version"] = "2.53.3"
extra["grpc-kotlin.version"] = "1.4.3"
extra["jetbrains-annotations.version"] = "26.0.2"
extra["jimfs.version"] = "1.3.1"
extra["json-schema-validator.version"] = "1.5.8"
extra["jts-core.version"] = "1.20.0"
extra["pitest.version"] = "1.20.1"
extra["reactor-grpc.version"] = "1.2.4"
extra["truth.version"] = "1.4.4"

// Dependencies from IO_GRPC_GRPC_JAVA_ARTIFACTS in https://github.com/grpc/grpc-java/blob/v1.68.1/repositories.bzl
extra["com.google.auto.value:auto-value.version"] = "1.11.0"
extra["org.apache.tomcat:annotations-api.version"] = "6.0.53"

dependencies {
    generate(enforcedPlatform("com.squareup.okhttp3:okhttp-bom:${property("okhttp.version")}"))
    generate(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:${property("kotlin.version")}"))
    generate(enforcedPlatform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:${property("kotlinx-coroutines.version")}"))
    generate(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:${property("spring-cloud.version")}"))
    generate(enforcedPlatform("org.springframework.boot:spring-boot-dependencies:${property("spring-boot.version")}"))
    generate(enforcedPlatform("software.amazon.awssdk:bom:${property("aws-sdk-v2.version")}"))
    generate(enforcedPlatform("com.google.protobuf:protobuf-bom:${property("protobuf-java.version")}"))
    generate(enforcedPlatform("io.grpc:grpc-bom:${property("grpc-java.version")}"))
    generate(enforcedPlatform("io.opentelemetry:opentelemetry-bom:${property("opentelemetry.version")}"))
    generate(enforcedPlatform("io.opentelemetry:opentelemetry-bom-alpha:${property("opentelemetry.version")}-alpha"))

    generate("ch.qos.logback:logback-classic")
    generate("com.azure:azure-storage-blob:${property("azure-storage-blob.version")}")
    generate("com.fasterxml.jackson.core:jackson-databind")
    generate("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    generate("com.fasterxml.jackson.module:jackson-module-kotlin")
    generate("com.github.docker-java:docker-java-core:${property("docker-java.version")}")
    generate("com.github.docker-java:docker-java-transport-zerodep:${property("docker-java.version")}")
    generate("com.google.api.grpc:proto-google-common-protos")
    generate("com.google.cloud:google-cloud-storage:${property("google-cloud-storage.version")}")
    generate("com.google.guava:guava")
    generate("com.google.jimfs:jimfs:${property("jimfs.version")}")
    generate("com.google.protobuf:protobuf-java")
    generate("com.google.protobuf:protobuf-kotlin")
    generate("com.google.truth:truth:${property("truth.version")}")
    generate("com.google.truth.extensions:truth-java8-extension:${property("truth.version")}")
    generate("com.google.truth.extensions:truth-proto-extension:${property("truth.version")}")
    generate("com.networknt:json-schema-validator:${property("json-schema-validator.version")}")
    generate("com.salesforce.servicelibs:reactive-grpc-gencommon:${property("reactor-grpc.version")}")
    generate("com.squareup.okhttp3:logging-interceptor")
    generate("com.squareup.okhttp3:mockwebserver3")
    generate("com.squareup.okhttp3:mockwebserver3-junit4")
    generate("com.squareup.okhttp3:mockwebserver3-junit5")
    generate("com.squareup.okhttp3:okhttp-jvm")
    generate("com.squareup.okio:okio-jvm")
    generate("dev.failsafe:failsafe:${property("failsafe.version")}")
    generate("io.grpc:grpc-core") { exclude("io.grpc", "grpc-util") }
    generate("io.grpc:grpc-kotlin-stub:${property("grpc-kotlin.version")}")
    generate("io.netty:netty-codec-socks")
    generate("io.netty:netty-handler-proxy")
    generate("io.projectreactor:reactor-core")
    generate("junit:junit")
    generate("org.apache.commons:commons-compress:${property("commons-compress.version")}")
    generate("org.awaitility:awaitility")
    generate("org.jetbrains:annotations:${property("jetbrains-annotations.version")}")
    generate("org.jetbrains.kotlin:kotlin-reflect")
    generate("org.jetbrains.kotlin:kotlin-stdlib")
    generate("org.jetbrains.kotlin:kotlin-stdlib-jdk7")
    generate("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    generate("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    generate("org.locationtech.jts:jts-core:${property("jts-core.version")}")
    generate("org.mockito:mockito-core")
    generate("org.pitest:pitest-aggregator:${property("pitest.version")}")
    generate("org.pitest:pitest-command-line:${property("pitest.version")}")
    generate("org.pitest:pitest-html-report:${property("pitest.version")}")
    generate("org.pitest:pitest:${property("pitest.version")}")
    generate("org.slf4j:slf4j-api")
    generate("org.springframework.boot:spring-boot")
    generate("org.springframework.cloud:spring-cloud-commons")
    generate("org.springframework:spring-core")
    generate("org.springframework:spring-web")
    generate("org.testcontainers:testcontainers")
    generate("software.amazon.awssdk:s3")

    // grpc-java dependencies
    generate("com.google.auto.value:auto-value:${property("com.google.auto.value:auto-value.version")}")
    generate("org.apache.tomcat:annotations-api:${property("org.apache.tomcat:annotations-api.version")}")
}

