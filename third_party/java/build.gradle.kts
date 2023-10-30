plugins {
    `java-base`
    id("com.github.zetten.bazel-dependencies-plugin") version "3.0.1"
    id("com.github.ben-manes.versions") version "0.50.0"
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
extra["aws-sdk-v2.version"] = "2.23.7"
extra["azure-sdk-bom.version"] = "1.2.19"
extra["google-cloud-libraries-bom.version"] = "26.30.0"
extra["grpc-java.version"] = "1.61.0"
extra["kotlin.version"] = "1.9.22"
extra["protobuf-java.version"] = "3.25.2"
extra["spring-boot.version"] = "3.2.2"
extra["spring-cloud.version"] = "2023.0.0"

//extra["asm.version"] = "9.6"
extra["commons-compress.version"] = "1.24.0"
extra["docker-java.version"] = "3.3.3"
extra["failsafe.version"] = "2.4.4"
//extra["google-common-protos.version"] = "2.27.0"
//extra["grpc-kotlin.version"] = "1.3.1"
extra["guava.version"] = "33.0.0-jre"
//extra["j2objc-annotations.version"] = "1.3"
extra["jimfs.version"] = "1.3.0"
extra["json-schema-validator.version"] = "1.0.87"
extra["jts-core.version"] = "1.19.0"
//extra["kotlin-coroutines.version"] = "1.6.4"
extra["okhttp.version"] = "4.12.0"
extra["pitest.version"] = "1.15.2"
extra["reactor-grpc.version"] = "1.2.4"
extra["truth.version"] = "1.3.0"

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
    generate("io.grpc:grpc-core") { exclude("io.grpc", "grpc-util") }
    generate("io.netty:netty-codec-socks")
    generate("io.netty:netty-handler-proxy")
    generate("io.projectreactor:reactor-core")
    generate("junit:junit")
    generate("net.jodah:failsafe:${property("failsafe.version")}")
    generate("org.apache.commons:commons-compress:${property("commons-compress.version")}")
    generate("org.awaitility:awaitility")
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
    generate("software.amazon.awssdk:s3")

    // grpc-java dependencies
    generate("com.google.auto.value:auto-value:${property("com.google.auto.value:auto-value.version")}")
    generate("org.apache.tomcat:annotations-api:${property("org.apache.tomcat:annotations-api.version")}")
}

//dependencyManagement {
//    imports {
//        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("spring-cloud.version")}")
//        mavenBom("org.springframework.boot:spring-boot-dependencies:${property("spring-boot.version")}")
//        mavenBom("software.amazon.awssdk:bom:${property("aws-sdk-v2.version")}")
//        mavenBom("com.azure:azure-sdk-bom:${property("azure-sdk-bom.version")}")
//        mavenBom("com.google.cloud:libraries-bom:${property("google-cloud-libraries-bom.version")}")
//        mavenBom("org.jetbrains.kotlin:kotlin-bom:${property("kotlin.version")}")
//        mavenBom("com.google.protobuf:protobuf-bom:${property("protobuf-java.version")}")
//        mavenBom("io.grpc:grpc-bom:${property("grpc-java.version")}")
//    }
//    dependencies {
//        dependency("com.github.docker-java:docker-java-core:${property("docker-java.version")}")
//        dependency("com.github.docker-java:docker-java-transport-zerodep:${property("docker-java.version")}")
//        dependency("com.google.api.grpc:proto-google-common-protos:${property("google-common-protos.version")}")
//        dependency("com.google.guava:guava:${property("guava.version")}")
//        dependency("com.google.j2objc:j2objc-annotations:${property("j2objc-annotations.version")}")
//        dependency("com.google.protobuf:protobuf-javalite:${property("protobuf-java.version")}")
//        dependency("com.google.protobuf:protobuf-kotlin:${property("protobuf-java.version")}")
//        dependency("com.google.truth.extensions:truth-java8-extension:${property("truth.version")}")
//        dependency("com.google.truth.extensions:truth-proto-extension:${property("truth.version")}")
//        dependency("com.google.truth:truth:${property("truth.version")}")
//        dependency("com.networknt:json-schema-validator")
//        dependency("com.salesforce.servicelibs:reactive-grpc-gencommon:${property("reactor-grpc.version")}")
//        dependency("com.squareup.okhttp3:logging-interceptor:${property("okhttp.version")}")
//        dependency("com.squareup.okhttp3:mockwebserver:${property("okhttp.version")}")
//        dependency("com.squareup.okhttp3:okhttp:${property("okhttp.version")}")
//        dependency("io.grpc:grpc-kotlin-stub:${property("grpc-kotlin.version")}")
//        dependency("net.jodah:failsafe")
//        dependency("org.apache.commons:commons-compress")
//        dependency("org.locationtech.jts:jts-core")
//        dependency("org.ow2.asm:asm:${property("asm.version")}")
//
//        // grpc-java dependencies
//        dependency("com.google.auto.value:auto-value:${property("com.google.auto.value:auto-value.version")}")
//        dependency("org.apache.tomcat:annotations-api:${property("org.apache.tomcat:annotations-api.version")}")
//    }
//}
//
//dependencies {
//    generate("ch.qos.logback:logback-classic")
//    generate("com.google.protobuf:protobuf-java")
//    generate("com.google.protobuf:protobuf-javalite")
//    generate("com.google.protobuf:protobuf-kotlin")
//    generate("com.google.truth.extensions:truth-java8-extension")
//    generate("com.google.truth.extensions:truth-proto-extension")
//    generate("com.google.truth:truth")
//    generate("io.grpc:grpc-alts")
//    generate("io.grpc:grpc-api")
//    generate("io.grpc:grpc-auth")
//    generate("io.grpc:grpc-census")
//    generate("io.grpc:grpc-context")
//    generate("io.grpc:grpc-core") {
//        exclude("io.grpc:grpc-util")
//    }
//    generate("io.grpc:grpc-netty")
//    generate("io.grpc:grpc-protobuf")
//    generate("io.grpc:grpc-protobuf-lite")
//    generate("io.grpc:grpc-services")
//    generate("io.grpc:grpc-stub")
//    generate("io.grpc:grpc-util")
//    generate("io.grpc:grpc-kotlin-stub")
//    generate("javax.annotation:javax.annotation-api")
//    generate("org.apache.tomcat:annotations-api")
//    generate("org.pitest:pitest")
//    generate("org.pitest:pitest-aggregator")
//    generate("org.pitest:pitest-command-line")
//    generate("org.pitest:pitest-html-report")
//    generate("org.springframework.boot:spring-boot")
//
//    // grpc-java deps
//    generate("com.google.auto.value:auto-value")
//    generate("org.apache.tomcat:annotations-api")
//}
