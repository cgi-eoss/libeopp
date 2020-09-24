plugins {
    base
    id("com.github.zetten.bazel-dependencies-plugin") version "1.7.3"
    id("com.github.ben-manes.versions") version "0.33.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
}

val generate by configurations.creating

bazelDependencies {
    configuration = generate
    outputFile = project.rootDir.resolve("java_repositories.bzl")
    mode = com.github.zetten.bazeldeps.BazelDependenciesMode.RULES_JVM_EXTERNAL
    createMavenInstallJson = true
    sourcesChecksums = true
    compileOnly = setOf(
        "com.google.auto.value:auto-value-annotations:1.7.2",
        "com.google.errorprone:error_prone_annotations:2.3.4",
        "org.codehaus.mojo:animal-sniffer-annotations:1.18",
        "javax.annotation:javax.annotation-api:1.3.2",
        "org.jetbrains:annotations:13.0",
        "org.apache.tomcat:annotations-api:6.0.53"
    )
}

repositories {
    jcenter()
}

extra["aws-sdk-v2.version"] = "2.14.23"
extra["commons-compress.version"] = "1.20"
extra["docker-java.version"] = "3.2.5"
extra["google-common-protos.version"] = "1.18.1"
extra["grpc-java.version"] = "1.32.1" // check org.apache.tomcat:annotations-api.version in https://github.com/grpc/grpc-java/blob/{GRPC_JAVA_VERSION}/repositories.bzl when updating
extra["guava.version"] = "29.0-jre"
extra["j2objc-annotations.version"] = "1.3"
extra["jimfs.version"] = "1.1"
extra["kotlin.version"] = "1.3.72"
extra["okhttp.version"] = "4.9.0"
extra["protobuf-java.version"] = "3.13.0"
extra["reactor-grpc.version"] = "1.0.1"
extra["spring-boot.version"] = "2.3.4.RELEASE"
extra["spring-cloud.version"] = "Hoxton.SR8"
extra["truth.version"] = "1.0.1"
extra["org.apache.tomcat:annotations-api.version"] = "6.0.53"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("spring-cloud.version")}")
        mavenBom("org.springframework.boot:spring-boot-dependencies:${property("spring-boot.version")}")
        mavenBom("com.google.protobuf:protobuf-bom:${property("protobuf-java.version")}")
        mavenBom("io.grpc:grpc-bom:${property("grpc-java.version")}")
        mavenBom("org.jetbrains.kotlin:kotlin-bom:${property("kotlin.version")}")
        mavenBom("software.amazon.awssdk:bom:${property("aws-sdk-v2.version")}")
    }
    dependencies {
        dependency("com.github.docker-java:docker-java-core:${property("docker-java.version")}")
        dependency("com.github.docker-java:docker-java-transport-zerodep:${property("docker-java.version")}")
        dependency("com.google.api.grpc:proto-google-common-protos:${property("google-common-protos.version")}")
        dependency("com.google.guava:guava:${property("guava.version")}")
        dependency("com.google.j2objc:j2objc-annotations:${property("j2objc-annotations.version")}")
        dependency("com.google.jimfs:jimfs:${property("jimfs.version")}")
        dependency("com.google.protobuf:protobuf-javalite:${property("protobuf-java.version")}")
        dependency("com.google.truth.extensions:truth-java8-extension:${property("truth.version")}")
        dependency("com.google.truth.extensions:truth-proto-extension:${property("truth.version")}")
        dependency("com.google.truth:truth:${property("truth.version")}")
        dependency("com.salesforce.servicelibs:reactive-grpc-gencommon:${property("reactor-grpc.version")}")
        dependency("com.squareup.okhttp3:logging-interceptor:${property("okhttp.version")}")
        dependency("com.squareup.okhttp3:mockwebserver:${property("okhttp.version")}")
        dependency("com.squareup.okhttp3:okhttp:${property("okhttp.version")}")
        dependency("org.apache.commons:commons-compress:${property("commons-compress.version")}")
        dependency("org.apache.tomcat:annotations-api:${property("org.apache.tomcat:annotations-api.version")}")
    }
}

dependencies {
    generate("ch.qos.logback:logback-classic")
    generate("com.github.docker-java:docker-java-core")
    generate("com.github.docker-java:docker-java-transport-zerodep")
    generate("com.fasterxml.jackson.core:jackson-databind")
    generate("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    generate("com.google.api.grpc:proto-google-common-protos")
    generate("com.google.guava:guava")
    generate("com.google.jimfs:jimfs")
    generate("com.google.protobuf:protobuf-java")
    generate("com.google.protobuf:protobuf-javalite")
    generate("com.google.truth.extensions:truth-java8-extension")
    generate("com.google.truth.extensions:truth-proto-extension")
    generate("com.google.truth:truth")
    generate("com.salesforce.servicelibs:reactive-grpc-gencommon")
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
    generate("org.awaitility:awaitility")
    generate("org.apache.commons:commons-compress")
    generate("org.apache.tomcat:annotations-api")
    generate("org.jetbrains.kotlin:kotlin-reflect")
    generate("org.jetbrains.kotlin:kotlin-stdlib")
    generate("org.mockito:mockito-core")
    generate("org.slf4j:slf4j-api")
    generate("org.springframework.cloud:spring-cloud-commons")
    generate("org.springframework:spring-core")
    generate("org.springframework:spring-web")
    generate("software.amazon.awssdk:s3")
}
