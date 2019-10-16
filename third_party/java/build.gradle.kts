plugins {
    base
    id("com.github.zetten.bazel-dependencies-plugin") version "1.4.0"
    id("com.github.ben-manes.versions") version "0.27.0"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
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

extra.set("google-common-protos.version", "1.17.0")
extra.set("grpc-java.version", "1.25.0")
extra.set("guava.version", "28.1-jre")
extra.set("j2objc-annotations.version", "1.3")
extra.set("kotlin.version", "1.3.61")
extra.set("okhttp.version", "4.2.2")
extra.set("protobuf-java.version", "3.11.1")
extra.set("spring-boot.version", "2.2.2.RELEASE")
extra.set("spring-cloud.version", "Hoxton.SR1")
extra.set("truth.version", "1.0")

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${extra.get("spring-cloud.version")}")
        mavenBom("org.springframework.boot:spring-boot-dependencies:${extra.get("spring-boot.version")}")
        mavenBom("com.google.protobuf:protobuf-bom:${extra.get("protobuf-java.version")}")
        mavenBom("io.grpc:grpc-bom:${extra.get("grpc-java.version")}")
        mavenBom("org.jetbrains.kotlin:kotlin-bom:${extra.get("kotlin.version")}")
    }
    dependencies {
        dependency("com.google.api.grpc:proto-google-common-protos:${extra.get("google-common-protos.version")}")
        dependency("com.google.guava:guava:${extra.get("guava.version")}")
        dependency("com.google.j2objc:j2objc-annotations:${extra.get("j2objc-annotations.version")}")
        dependency("com.google.truth.extensions:truth-java8-extension:${extra.get("truth.version")}")
        dependency("com.google.truth.extensions:truth-proto-extension:${extra.get("truth.version")}")
        dependency("com.google.truth:truth:${extra.get("truth.version")}")
        dependency("com.squareup.okhttp3:logging-interceptor:${extra.get("okhttp.version")}")
        dependency("com.squareup.okhttp3:mockwebserver:${extra.get("okhttp.version")}")
        dependency("com.squareup.okhttp3:okhttp:${extra.get("okhttp.version")}")
    }
}

dependencies {
    generate("ch.qos.logback:logback-classic")
    generate("com.google.api.grpc:proto-google-common-protos")
    generate("com.google.guava:guava")
    generate("com.google.protobuf:protobuf-java")
    generate("com.google.truth.extensions:truth-java8-extension")
    generate("com.google.truth.extensions:truth-proto-extension")
    generate("com.google.truth:truth")
    generate("com.squareup.okhttp3:logging-interceptor")
    generate("com.squareup.okhttp3:mockwebserver")
    generate("com.squareup.okhttp3:okhttp")
    generate("io.grpc:grpc-context")
    generate("io.grpc:grpc-core")
    generate("io.grpc:grpc-netty")
    generate("io.grpc:grpc-protobuf")
    generate("io.grpc:grpc-protobuf-lite")
    generate("io.grpc:grpc-services")
    generate("io.grpc:grpc-stub")
    generate("io.projectreactor:reactor-core")
    generate("javax.annotation:javax.annotation-api")
    generate("junit:junit")
    generate("org.jetbrains.kotlin:kotlin-reflect")
    generate("org.jetbrains.kotlin:kotlin-stdlib")
    generate("org.mockito:mockito-core")
    generate("org.slf4j:slf4j-api")
    generate("org.springframework.cloud:spring-cloud-commons")
    generate("org.springframework:spring-core")
}
