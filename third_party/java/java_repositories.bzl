load("@rules_jvm_external//:specs.bzl", "maven")

REPOSITORIES = [
    "https://repo.maven.apache.org/maven2/",
]

ARTIFACTS = [
    maven.artifact("ch.qos.logback", "logback-classic", "1.2.12"),
    maven.artifact("ch.qos.logback", "logback-core", "1.2.12"),
    maven.artifact("com.azure", "azure-core", "1.38.0"),
    maven.artifact("com.azure", "azure-core-http-netty", "1.13.2"),
    maven.artifact("com.azure", "azure-json", "1.0.0"),
    maven.artifact("com.azure", "azure-storage-blob", "12.22.0"),
    maven.artifact("com.azure", "azure-storage-common", "12.21.0"),
    maven.artifact("com.azure", "azure-storage-internal-avro", "12.7.0"),
    maven.artifact("com.ethlo.time", "itu", "1.7.0"),
    maven.artifact("com.fasterxml.jackson.core", "jackson-annotations", "2.13.5"),
    maven.artifact("com.fasterxml.jackson.core", "jackson-core", "2.13.5"),
    maven.artifact("com.fasterxml.jackson.core", "jackson-databind", "2.13.5"),
    maven.artifact("com.fasterxml.jackson.dataformat", "jackson-dataformat-xml", "2.13.5"),
    maven.artifact("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", "2.13.5"),
    maven.artifact("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", "2.13.5"),
    maven.artifact("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.13.5"),
    maven.artifact("com.fasterxml.woodstox", "woodstox-core", "6.4.0"),
    maven.artifact("com.github.docker-java", "docker-java-api", "3.3.0"),
    maven.artifact("com.github.docker-java", "docker-java-core", "3.3.0"),
    maven.artifact("com.github.docker-java", "docker-java-transport", "3.3.0"),
    maven.artifact("com.github.docker-java", "docker-java-transport-zerodep", "3.3.0"),
    maven.artifact("com.google.android", "annotations", "4.1.1.4"),
    maven.artifact("com.google.api", "api-common", "2.10.0"),
    maven.artifact("com.google.api-client", "google-api-client", "2.2.0"),
    maven.artifact("com.google.api", "gax", "2.27.0"),
    maven.artifact("com.google.api", "gax-grpc", "2.27.0"),
    maven.artifact("com.google.api", "gax-httpjson", "0.112.0"),
    maven.artifact("com.google.api.grpc", "gapic-google-cloud-storage-v2", "2.22.2-alpha"),
    maven.artifact("com.google.api.grpc", "grpc-google-cloud-storage-v2", "2.22.2-alpha"),
    maven.artifact("com.google.api.grpc", "proto-google-cloud-storage-v2", "2.22.2-alpha"),
    maven.artifact("com.google.api.grpc", "proto-google-common-protos", "2.18.0"),
    maven.artifact("com.google.api.grpc", "proto-google-iam-v1", "1.13.0"),
    maven.artifact("com.google.apis", "google-api-services-storage", "v1-rev20230301-2.0.0"),
    maven.artifact("com.google.auth", "google-auth-library-credentials", "1.16.0"),
    maven.artifact("com.google.auth", "google-auth-library-oauth2-http", "1.16.0"),
    maven.artifact("com.google.auto.value", "auto-value-annotations", "1.10.1"),
    maven.artifact("com.google.cloud", "google-cloud-core", "2.17.0"),
    maven.artifact("com.google.cloud", "google-cloud-core-grpc", "2.17.0"),
    maven.artifact("com.google.cloud", "google-cloud-core-http", "2.17.0"),
    maven.artifact("com.google.cloud", "google-cloud-storage", "2.22.2"),
    maven.artifact("com.google.code.findbugs", "jsr305", "3.0.2"),
    maven.artifact("com.google.code.gson", "gson", "2.10.1"),
    maven.artifact("com.google.errorprone", "error_prone_annotations", "2.18.0"),
    maven.artifact("com.google.guava", "failureaccess", "1.0.1"),
    maven.artifact("com.google.guava", "guava", "31.1-jre"),
    maven.artifact("com.google.guava", "listenablefuture", "9999.0-empty-to-avoid-conflict-with-guava"),
    maven.artifact("com.google.http-client", "google-http-client", "1.43.1"),
    maven.artifact("com.google.http-client", "google-http-client-apache-v2", "1.43.1"),
    maven.artifact("com.google.http-client", "google-http-client-appengine", "1.43.1"),
    maven.artifact("com.google.http-client", "google-http-client-gson", "1.43.1"),
    maven.artifact("com.google.http-client", "google-http-client-jackson2", "1.43.1"),
    maven.artifact("com.google.j2objc", "j2objc-annotations", "1.3"),
    maven.artifact("com.google.jimfs", "jimfs", "1.2"),
    maven.artifact("com.google.oauth-client", "google-oauth-client", "1.34.1"),
    maven.artifact("com.google.protobuf", "protobuf-java", "3.21.12"),
    maven.artifact("com.google.protobuf", "protobuf-java-util", "3.21.12"),
    maven.artifact("com.google.protobuf", "protobuf-javalite", "3.21.12"),
    maven.artifact("com.google.protobuf", "protobuf-kotlin", "3.21.12"),
    maven.artifact("com.google.re2j", "re2j", "1.6"),
    maven.artifact("com.google.truth.extensions", "truth-java8-extension", "1.1.3"),
    maven.artifact("com.google.truth.extensions", "truth-liteproto-extension", "1.1.3"),
    maven.artifact("com.google.truth.extensions", "truth-proto-extension", "1.1.3"),
    maven.artifact("com.google.truth", "truth", "1.1.3"),
    maven.artifact("com.networknt", "json-schema-validator", "1.0.81"),
    maven.artifact("com.salesforce.servicelibs", "grpc-contrib", "0.8.1"),
    maven.artifact("com.salesforce.servicelibs", "jprotoc", "1.2.2"),
    maven.artifact("com.salesforce.servicelibs", "reactive-grpc-gencommon", "1.2.4"),
    maven.artifact("com.squareup.okhttp3", "logging-interceptor", "4.10.0"),
    maven.artifact("com.squareup.okhttp3", "mockwebserver", "4.10.0"),
    maven.artifact("com.squareup.okhttp3", "okhttp", "4.10.0"),
    maven.artifact("com.squareup.okio", "okio-jvm", "3.0.0"),
    maven.artifact("commons-codec", "commons-codec", "1.15"),
    maven.artifact("commons-io", "commons-io", "2.6"),
    maven.artifact("io.grpc", "grpc-alts", "1.55.1"),
    maven.artifact("io.grpc", "grpc-api", "1.55.1"),
    maven.artifact("io.grpc", "grpc-auth", "1.55.1"),
    maven.artifact("io.grpc", "grpc-census", "1.55.1"),
    maven.artifact("io.grpc", "grpc-context", "1.55.1"),
    maven.artifact("io.grpc", "grpc-core", "1.55.1"),
    maven.artifact("io.grpc", "grpc-googleapis", "1.55.1"),
    maven.artifact("io.grpc", "grpc-grpclb", "1.55.1"),
    maven.artifact("io.grpc", "grpc-kotlin-stub", "1.3.0"),
    maven.artifact("io.grpc", "grpc-netty", "1.55.1"),
    maven.artifact("io.grpc", "grpc-netty-shaded", "1.55.1"),
    maven.artifact("io.grpc", "grpc-protobuf", "1.55.1"),
    maven.artifact("io.grpc", "grpc-protobuf-lite", "1.55.1"),
    maven.artifact("io.grpc", "grpc-rls", "1.55.1"),
    maven.artifact("io.grpc", "grpc-services", "1.55.1"),
    maven.artifact("io.grpc", "grpc-stub", "1.55.1"),
    maven.artifact("io.grpc", "grpc-xds", "1.55.1"),
    maven.artifact("io.netty", "netty-buffer", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-codec", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-codec-dns", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-codec-http", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-codec-http2", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-codec-socks", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-common", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-handler", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-handler-proxy", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-resolver", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-resolver-dns", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-resolver-dns-classes-macos", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-resolver-dns-native-macos", "4.1.92.Final", classifier = "osx-x86_64"),
    maven.artifact("io.netty", "netty-tcnative-boringssl-static", "2.0.60.Final"),
    maven.artifact("io.netty", "netty-tcnative-classes", "2.0.60.Final"),
    maven.artifact("io.netty", "netty-transport", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-transport-classes-epoll", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-transport-classes-kqueue", "4.1.92.Final"),
    maven.artifact("io.netty", "netty-transport-native-epoll", "4.1.92.Final", classifier = "linux-x86_64"),
    maven.artifact("io.netty", "netty-transport-native-kqueue", "4.1.92.Final", classifier = "osx-x86_64"),
    maven.artifact("io.netty", "netty-transport-native-unix-common", "4.1.92.Final"),
    maven.artifact("io.opencensus", "opencensus-api", "0.31.1"),
    maven.artifact("io.opencensus", "opencensus-contrib-grpc-metrics", "0.31.0"),
    maven.artifact("io.opencensus", "opencensus-contrib-http-util", "0.31.1"),
    maven.artifact("io.opencensus", "opencensus-proto", "0.2.0"),
    maven.artifact("io.perfmark", "perfmark-api", "0.26.0"),
    maven.artifact("io.projectreactor.netty", "reactor-netty-core", "1.0.32"),
    maven.artifact("io.projectreactor.netty", "reactor-netty-http", "1.0.32"),
    maven.artifact("io.projectreactor", "reactor-core", "3.4.29"),
    maven.artifact("javax.annotation", "javax.annotation-api", "1.3.2"),
    maven.artifact("junit", "junit", "4.13.2"),
    maven.artifact("net.bytebuddy", "byte-buddy", "1.12.23"),
    maven.artifact("net.bytebuddy", "byte-buddy-agent", "1.12.23"),
    maven.artifact("net.java.dev.jna", "jna", "5.12.1"),
    maven.artifact("net.java.dev.jna", "jna-platform", "5.9.0"),
    maven.artifact("net.jodah", "failsafe", "2.4.4"),
    maven.artifact("org.apache.commons", "commons-compress", "1.22"),
    maven.artifact("org.apache.commons", "commons-lang3", "3.12.0"),
    maven.artifact("org.apache.commons", "commons-text", "1.10.0"),
    maven.artifact("org.apache.httpcomponents", "httpclient", "4.5.14"),
    maven.artifact("org.apache.httpcomponents", "httpcore", "4.4.16"),
    maven.artifact("org.apache.tomcat", "annotations-api", "6.0.53"),
    maven.artifact("org.awaitility", "awaitility", "4.2.0"),
    maven.artifact("org.bouncycastle", "bcpkix-jdk15on", "1.64"),
    maven.artifact("org.bouncycastle", "bcprov-jdk15on", "1.64"),
    maven.artifact("org.checkerframework", "checker-qual", "3.32.0"),
    maven.artifact("org.codehaus.mojo", "animal-sniffer-annotations", "1.23"),
    maven.artifact("org.codehaus.woodstox", "stax2-api", "4.2.1"),
    maven.artifact("org.conscrypt", "conscrypt-openjdk-uber", "2.5.2"),
    maven.artifact("org.hamcrest", "hamcrest", "2.2"),
    maven.artifact("org.hamcrest", "hamcrest-core", "2.2"),
    maven.artifact("org.jetbrains", "annotations", "13.0"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-reflect", "1.7.21"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-stdlib", "1.7.21"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-stdlib-common", "1.7.21"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-stdlib-jdk7", "1.7.21"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "1.7.21"),
    maven.artifact("org.jetbrains.kotlinx", "kotlinx-coroutines-core-jvm", "1.6.4"),
    maven.artifact("org.jetbrains.kotlinx", "kotlinx-coroutines-debug", "1.6.4"),
    maven.artifact("org.locationtech.jts", "jts-core", "1.19.0"),
    maven.artifact("org.mockito", "mockito-core", "4.5.1"),
    maven.artifact("org.objenesis", "objenesis", "3.2"),
    maven.artifact("org.ow2.asm", "asm", "9.1"),
    maven.artifact("org.pitest", "pitest", "1.13.2"),
    maven.artifact("org.pitest", "pitest-aggregator", "1.13.2"),
    maven.artifact("org.pitest", "pitest-command-line", "1.13.2"),
    maven.artifact("org.pitest", "pitest-entry", "1.13.2"),
    maven.artifact("org.pitest", "pitest-html-report", "1.13.2"),
    maven.artifact("org.reactivestreams", "reactive-streams", "1.0.4"),
    maven.artifact("org.slf4j", "slf4j-api", "1.7.36"),
    maven.artifact("org.springframework.boot", "spring-boot", "2.7.12"),
    maven.artifact("org.springframework.cloud", "spring-cloud-commons", "3.1.6"),
    maven.artifact("org.springframework.security", "spring-security-crypto", "5.7.8"),
    maven.artifact("org.springframework", "spring-aop", "5.3.27"),
    maven.artifact("org.springframework", "spring-beans", "5.3.27"),
    maven.artifact("org.springframework", "spring-context", "5.3.27"),
    maven.artifact("org.springframework", "spring-core", "5.3.27"),
    maven.artifact("org.springframework", "spring-expression", "5.3.27"),
    maven.artifact("org.springframework", "spring-jcl", "5.3.27"),
    maven.artifact("org.springframework", "spring-web", "5.3.27"),
    maven.artifact("org.threeten", "threetenbp", "1.6.8"),
    maven.artifact("org.yaml", "snakeyaml", "1.30"),
    maven.artifact("software.amazon.awssdk", "annotations", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "apache-client", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "arns", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "auth", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "aws-core", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "aws-query-protocol", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "aws-xml-protocol", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "crt-core", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "endpoints-spi", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "http-client-spi", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "json-utils", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "metrics-spi", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "netty-nio-client", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "profiles", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "protocol-core", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "regions", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "s3", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "sdk-core", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "third-party-jackson-core", "2.20.67"),
    maven.artifact("software.amazon.awssdk", "utils", "2.20.67"),
    maven.artifact("software.amazon.eventstream", "eventstream", "1.0.1"),
]
