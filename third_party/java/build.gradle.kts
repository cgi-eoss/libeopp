plugins {
    base
    id("com.github.zetten.bazel-dependencies-plugin") version "1.7.1"
    id("com.github.ben-manes.versions") version "0.27.0"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

val generate by configurations.creating

bazelDependencies {
    configuration = generate
    outputFile = project.rootDir.resolve("java_repositories.bzl")
    mode = com.github.zetten.bazeldeps.BazelDependenciesMode.RULES_JVM_EXTERNAL
    createMavenInstallJson = true
    sourcesChecksums = true
    compileOnly = setOf(
        "com.google.auto.value:auto-value-annotations:1.6.3",
        "com.google.errorprone:error_prone_annotations:2.3.4",
        "org.codehaus.mojo:animal-sniffer-annotations:1.17",
        "javax.annotation:javax.annotation-api:1.3.2",
        "org.jetbrains:annotations:13.0"
    )
    testOnly = setOf(
        "com.google.truth.extensions:truth-java8-extension:1.0.1",
        "com.google.truth.extensions:truth-liteproto-extension:1.0.1",
        "com.google.truth.extensions:truth-proto-extension:1.0.1",
        "com.google.truth:truth:1.0.1",
        "com.squareup.okhttp3:mockwebserver:4.3.1",
        "junit:junit:4.12",
        "org.hamcrest:hamcrest:2.1",
        "org.hamcrest:hamcrest-core:2.1",
        "org.mockito:mockito-core:3.1.0"
    )
}

repositories {
    jcenter()
}

extra.set("aws-sdk-v2.version", "2.10.68")
extra.set("google-common-protos.version", "1.17.0")
extra.set("grpc-java.version", "1.26.0")
extra.set("guava.version", "28.2-jre")
extra.set("j2objc-annotations.version", "1.3")
extra.set("kotlin.version", "1.3.61")
extra.set("okhttp.version", "4.3.1")
extra.set("protobuf-java.version", "3.11.1")
extra.set("reactor-grpc.version", "1.0.0")
extra.set("spring-boot.version", "2.2.4.RELEASE")
extra.set("spring-cloud.version", "Hoxton.SR1")
extra.set("truth.version", "1.0.1")

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${extra.get("spring-cloud.version")}")
        mavenBom("org.springframework.boot:spring-boot-dependencies:${extra.get("spring-boot.version")}")
        mavenBom("com.google.protobuf:protobuf-bom:${extra.get("protobuf-java.version")}")
        mavenBom("io.grpc:grpc-bom:${extra.get("grpc-java.version")}")
        mavenBom("org.jetbrains.kotlin:kotlin-bom:${extra.get("kotlin.version")}")
        mavenBom("software.amazon.awssdk:bom:${extra.get("aws-sdk-v2.version")}")
    }
    dependencies {
        dependency("com.google.api.grpc:proto-google-common-protos:${extra.get("google-common-protos.version")}")
        dependency("com.google.guava:guava:${extra.get("guava.version")}")
        dependency("com.google.j2objc:j2objc-annotations:${extra.get("j2objc-annotations.version")}")
        dependency("com.google.protobuf:protobuf-javalite:${extra.get("protobuf-java.version")}")
        dependency("com.google.truth.extensions:truth-java8-extension:${extra.get("truth.version")}")
        dependency("com.google.truth.extensions:truth-proto-extension:${extra.get("truth.version")}")
        dependency("com.google.truth:truth:${extra.get("truth.version")}")
        dependency("com.salesforce.servicelibs:reactive-grpc-gencommon:${extra.get("reactor-grpc.version")}")
        dependency("com.squareup.okhttp3:logging-interceptor:${extra.get("okhttp.version")}")
        dependency("com.squareup.okhttp3:mockwebserver:${extra.get("okhttp.version")}")
        dependency("com.squareup.okhttp3:okhttp:${extra.get("okhttp.version")}")
    }
}

dependencies {
    generate("ch.qos.logback:logback-classic")
    generate("com.fasterxml.jackson.core:jackson-databind")
    generate("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    generate("com.google.api.grpc:proto-google-common-protos")
    generate("com.google.guava:guava")
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
    generate("org.jetbrains.kotlin:kotlin-reflect")
    generate("org.jetbrains.kotlin:kotlin-stdlib")
    generate("org.mockito:mockito-core")
    generate("org.slf4j:slf4j-api")
    generate("org.springframework.cloud:spring-cloud-commons")
    generate("org.springframework:spring-core")
    generate("org.springframework:spring-web")
    generate("software.amazon.awssdk:s3")
}
