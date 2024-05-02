plugins {
    `java-base`
    id("com.github.zetten.bazel-dependencies-plugin") version "3.0.1"
    id("com.github.ben-manes.versions") version "0.51.0"
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
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
        version = "6.0"
    }
}

repositories {
    mavenCentral()
}

// BOMs
extra["aws-sdk-v2.version"] = "2.25.40"
extra["azure-sdk-bom.version"] = "1.2.23"
extra["google-cloud-libraries-bom.version"] = "26.37.0"
extra["grpc-java.version"] = "1.63.0"
extra["kotlin.version"] = "1.9.23"
extra["okhttp.version"] = "4.12.0"
extra["protobuf-java.version"] = "3.25.3"
extra["spring-boot.version"] = "3.2.5"
extra["spring-cloud.version"] = "2023.0.1"

extra["commons-compress.version"] = "1.26.1"
extra["docker-java.version"] = "3.3.6"
extra["failsafe.version"] = "3.3.2"
extra["grpc-kotlin.version"] = "1.4.1"
extra["guava.version"] = "33.1.0-jre"
extra["jetbrains-annotations.version"] = "24.1.0"
extra["jimfs.version"] = "1.3.0"
extra["json-schema-validator.version"] = "1.4.0"
extra["jts-core.version"] = "1.19.0"
extra["pitest.version"] = "1.16.0"
extra["reactor-grpc.version"] = "1.2.4"
extra["truth.version"] = "1.4.2"

// Dependencies from grpc-java
extra["com.google.auto.value:auto-value.version"] = "1.10.4"
extra["org.apache.tomcat:annotations-api.version"] = "6.0.53"

dependencies {
    generate(platform("org.springframework.cloud:spring-cloud-dependencies:${property("spring-cloud.version")}"))
    generate(platform("org.springframework.boot:spring-boot-dependencies:${property("spring-boot.version")}"))
    generate(platform("software.amazon.awssdk:bom:${property("aws-sdk-v2.version")}"))
    generate(platform("com.azure:azure-sdk-bom:${property("azure-sdk-bom.version")}"))
    generate(platform("com.google.cloud:libraries-bom:${property("google-cloud-libraries-bom.version")}"))
    generate(platform("com.squareup.okhttp3:okhttp-bom:${property("okhttp.version")}"))
    generate(platform("org.jetbrains.kotlin:kotlin-bom:${property("kotlin.version")}"))
    generate(platform("com.google.protobuf:protobuf-bom:${property("protobuf-java.version")}"))
    generate(platform("io.grpc:grpc-bom:${property("grpc-java.version")}"))

    generate("ch.qos.logback:logback-classic")
    generate("com.azure:azure-storage-blob")
    generate("com.fasterxml.jackson.core:jackson-databind")
    generate("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    generate("com.fasterxml.jackson.module:jackson-module-kotlin")
    generate("com.github.docker-java:docker-java-core:${property("docker-java.version")}")
    generate("com.github.docker-java:docker-java-transport-zerodep:${property("docker-java.version")}")
    generate("com.google.api.grpc:proto-google-common-protos")
    generate("com.google.cloud:google-cloud-storage")
    generate("com.google.guava:guava:${property("guava.version")}")
    generate("com.google.jimfs:jimfs:${property("jimfs.version")}")
    generate("com.google.protobuf:protobuf-java")
    generate("com.google.protobuf:protobuf-kotlin")
    generate("com.google.truth:truth:${property("truth.version")}")
    generate("com.google.truth.extensions:truth-java8-extension:${property("truth.version")}")
    generate("com.google.truth.extensions:truth-proto-extension:${property("truth.version")}")
    generate("com.networknt:json-schema-validator:${property("json-schema-validator.version")}")
    generate("com.salesforce.servicelibs:reactive-grpc-gencommon:${property("reactor-grpc.version")}")
    generate("com.squareup.okhttp3:logging-interceptor")
    generate("com.squareup.okhttp3:mockwebserver")
    generate("com.squareup.okhttp3:okhttp")
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

