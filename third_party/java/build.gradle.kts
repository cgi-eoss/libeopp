plugins {
    base
    id("com.github.zetten.bazel-dependencies-plugin") version "2.2.0"
    id("com.github.ben-manes.versions") version "0.44.0"
    id("io.spring.dependency-management") version "1.1.0"
}

val generate by configurations.creating {
    attributes {
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
    }
}

bazelDependencies {
    configuration.set(generate)
    outputFile.set(project.rootDir.resolve("java_repositories.bzl"))
    sourcesChecksums.set(true)
    rulesJvmExternalVersion.set("4.5.0")
    createMavenInstallJson.set(true)
}

repositories {
    mavenCentral()
}

extra["aws-sdk-v2.version"] = "2.19.17"
extra["azure-sdk-bom.version"] = "1.2.8"
extra["commons-compress.version"] = "1.22"
extra["docker-java.version"] = "3.2.14"
extra["failsafe.version"] = "2.4.4"
extra["google-cloud-libraries-bom.version"] = "26.3.0"
extra["google-common-protos.version"] = "2.12.0"
extra["grpc-java.version"] = "1.52.1" // check org.apache.tomcat:annotations-api.version in https://github.com/grpc/grpc-java/blob/{GRPC_JAVA_VERSION}/repositories.bzl when updating
extra["grpc-kotlin.version"] = "1.3.0"
extra["guava.version"] = "31.1-jre"
extra["j2objc-annotations.version"] = "1.3"
extra["jimfs.version"] = "1.2"
extra["json-schema-validator.version"] = "1.0.75"
extra["jts-core.version"] = "1.19.0"
extra["kotlin.version"] = "1.7.21"
extra["kotlin-coroutines.version"] = "1.6.4"
extra["okhttp.version"] = "4.10.0"
extra["pitest.version"] = "1.10.3"
extra["protobuf-java.version"] = "3.21.12"
extra["reactor-grpc.version"] = "1.2.3"
extra["spring-boot.version"] = "2.7.7"
extra["spring-cloud.version"] = "2021.0.5"
extra["truth.version"] = "1.1.3"
extra["org.apache.tomcat:annotations-api.version"] = "6.0.53"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("spring-cloud.version")}")
        mavenBom("org.springframework.boot:spring-boot-dependencies:${property("spring-boot.version")}")
        mavenBom("com.google.protobuf:protobuf-bom:${property("protobuf-java.version")}")
        mavenBom("software.amazon.awssdk:bom:${property("aws-sdk-v2.version")}")
        mavenBom("com.azure:azure-sdk-bom:${property("azure-sdk-bom.version")}")
        mavenBom("com.google.cloud:libraries-bom:${property("google-cloud-libraries-bom.version")}")
        mavenBom("org.jetbrains.kotlin:kotlin-bom:${property("kotlin.version")}")
        mavenBom("io.grpc:grpc-bom:${property("grpc-java.version")}")
    }
    dependencies {
        dependency("com.github.docker-java:docker-java-core:${property("docker-java.version")}")
        dependency("com.github.docker-java:docker-java-transport-zerodep:${property("docker-java.version")}")
        dependency("com.google.api.grpc:proto-google-common-protos:${property("google-common-protos.version")}")
        dependency("com.google.guava:guava:${property("guava.version")}")
        dependency("com.google.j2objc:j2objc-annotations:${property("j2objc-annotations.version")}")
        dependency("com.google.jimfs:jimfs:${property("jimfs.version")}")
        dependency("com.google.protobuf:protobuf-javalite:${property("protobuf-java.version")}")
        dependency("com.google.protobuf:protobuf-kotlin:${property("protobuf-java.version")}")
        dependency("com.google.truth.extensions:truth-java8-extension:${property("truth.version")}")
        dependency("com.google.truth.extensions:truth-proto-extension:${property("truth.version")}")
        dependency("com.google.truth:truth:${property("truth.version")}")
        dependency("com.networknt:json-schema-validator:${property("json-schema-validator.version")}")
        dependency("com.salesforce.servicelibs:reactive-grpc-gencommon:${property("reactor-grpc.version")}")
        dependency("com.squareup.okhttp3:logging-interceptor:${property("okhttp.version")}")
        dependency("com.squareup.okhttp3:mockwebserver:${property("okhttp.version")}")
        dependency("com.squareup.okhttp3:okhttp:${property("okhttp.version")}")
        dependency("io.grpc:grpc-kotlin-stub:${property("grpc-kotlin.version")}")
        dependency("net.jodah:failsafe:${property("failsafe.version")}")
        dependency("org.apache.commons:commons-compress:${property("commons-compress.version")}")
        dependency("org.apache.tomcat:annotations-api:${property("org.apache.tomcat:annotations-api.version")}")
        dependency("org.locationtech.jts:jts-core:${property("jts-core.version")}")
        dependency("org.pitest:pitest-aggregator:${property("pitest.version")}")
        dependency("org.pitest:pitest-command-line:${property("pitest.version")}")
        dependency("org.pitest:pitest-html-report:${property("pitest.version")}")
        dependency("org.pitest:pitest:${property("pitest.version")}")
    }
}

dependencies {
    generate("ch.qos.logback:logback-classic")
    generate("com.azure:azure-storage-blob")
    generate("com.fasterxml.jackson.core:jackson-databind")
    generate("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    generate("com.fasterxml.jackson.module:jackson-module-kotlin")
    generate("com.github.docker-java:docker-java-core")
    generate("com.github.docker-java:docker-java-transport-zerodep")
    generate("com.google.api.grpc:proto-google-common-protos")
    generate("com.google.cloud:google-cloud-storage")
    generate("com.google.guava:guava")
    generate("com.google.jimfs:jimfs")
    generate("com.google.protobuf:protobuf-java")
    generate("com.google.protobuf:protobuf-javalite")
    generate("com.google.protobuf:protobuf-kotlin")
    generate("com.google.truth.extensions:truth-java8-extension")
    generate("com.google.truth.extensions:truth-proto-extension")
    generate("com.google.truth:truth")
    generate("com.networknt:json-schema-validator")
    generate("com.salesforce.servicelibs:reactive-grpc-gencommon")
    generate("com.squareup.okhttp3:logging-interceptor")
    generate("com.squareup.okhttp3:mockwebserver")
    generate("com.squareup.okhttp3:okhttp")
    generate("com.squareup.okio:okio-jvm")
    generate("io.grpc:grpc-alts")
    generate("io.grpc:grpc-api")
    generate("io.grpc:grpc-auth")
    generate("io.grpc:grpc-census")
    generate("io.grpc:grpc-context")
    generate("io.grpc:grpc-core")
    generate("io.grpc:grpc-netty")
    generate("io.grpc:grpc-protobuf")
    generate("io.grpc:grpc-protobuf-lite")
    generate("io.grpc:grpc-services")
    generate("io.grpc:grpc-stub")
    generate("io.grpc:grpc-kotlin-stub")
    generate("io.projectreactor:reactor-core")
    generate("javax.annotation:javax.annotation-api")
    generate("junit:junit")
    generate("net.jodah:failsafe")
    generate("org.apache.commons:commons-compress")
    generate("org.apache.tomcat:annotations-api")
    generate("org.awaitility:awaitility")
    generate("org.jetbrains.kotlin:kotlin-reflect")
    generate("org.jetbrains.kotlin:kotlin-stdlib")
    generate("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    generate("org.jetbrains.kotlinx:kotlinx-coroutines-debug")
    generate("org.locationtech.jts:jts-core")
    generate("org.mockito:mockito-core")
    generate("org.pitest:pitest")
    generate("org.pitest:pitest-aggregator")
    generate("org.pitest:pitest-command-line")
    generate("org.pitest:pitest-html-report")
    generate("org.slf4j:slf4j-api")
    generate("org.springframework.boot:spring-boot")
    generate("org.springframework.cloud:spring-cloud-commons")
    generate("org.springframework:spring-core")
    generate("org.springframework:spring-web")
    generate("software.amazon.awssdk:s3")
}
