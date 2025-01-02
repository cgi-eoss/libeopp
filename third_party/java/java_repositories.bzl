load("@rules_jvm_external//:specs.bzl", "maven")

REPOSITORIES = ["https://repo.maven.apache.org/maven2/"]

ARTIFACTS = [
    maven.artifact("antlr", "antlr", "2.7.7"),
    maven.artifact("ch.qos.logback", "logback-classic", "1.5.12"),
    maven.artifact("ch.qos.logback", "logback-core", "1.5.12"),
    maven.artifact("com.azure", "azure-core", "1.52.0"),
    maven.artifact("com.azure", "azure-core-http-netty", "1.15.4"),
    maven.artifact("com.azure", "azure-json", "1.3.0"),
    maven.artifact("com.azure", "azure-storage-blob", "12.28.1"),
    maven.artifact("com.azure", "azure-storage-common", "12.27.1"),
    maven.artifact("com.azure", "azure-storage-internal-avro", "12.13.0"),
    maven.artifact("com.azure", "azure-xml", "1.1.0"),
    maven.artifact("com.ethlo.time", "itu", "1.10.2"),
    maven.artifact("com.fasterxml.jackson.core", "jackson-annotations", "2.18.2"),
    maven.artifact("com.fasterxml.jackson.core", "jackson-core", "2.18.2"),
    maven.artifact("com.fasterxml.jackson.core", "jackson-databind", "2.18.2"),
    maven.artifact("com.fasterxml.jackson.dataformat", "jackson-dataformat-xml", "2.18.2"),
    maven.artifact("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", "2.18.2"),
    maven.artifact("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", "2.18.2"),
    maven.artifact("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.18.2"),
    maven.artifact("com.fasterxml.woodstox", "woodstox-core", "7.0.0"),
    maven.artifact("com.github.docker-java", "docker-java-api", "3.4.1"),
    maven.artifact("com.github.docker-java", "docker-java-core", "3.4.1"),
    maven.artifact("com.github.docker-java", "docker-java-transport", "3.4.1"),
    maven.artifact("com.github.docker-java", "docker-java-transport-zerodep", "3.4.1"),
    maven.artifact("com.google.android", "annotations", "4.1.1.4"),
    maven.artifact("com.google.api", "api-common", "2.41.0"),
    maven.artifact("com.google.api", "gax", "2.58.0"),
    maven.artifact("com.google.api", "gax-grpc", "2.58.0"),
    maven.artifact("com.google.api", "gax-httpjson", "2.58.0"),
    maven.artifact("com.google.api-client", "google-api-client", "2.7.0"),
    maven.artifact("com.google.api.grpc", "gapic-google-cloud-storage-v2", "2.45.0-beta"),
    maven.artifact("com.google.api.grpc", "grpc-google-cloud-storage-v2", "2.45.0-beta"),
    maven.artifact("com.google.api.grpc", "proto-google-cloud-monitoring-v3", "3.31.0"),
    maven.artifact("com.google.api.grpc", "proto-google-cloud-storage-v2", "2.45.0-beta"),
    maven.artifact("com.google.api.grpc", "proto-google-common-protos", "2.49.0"),
    maven.artifact("com.google.api.grpc", "proto-google-iam-v1", "1.44.0"),
    maven.artifact("com.google.apis", "google-api-services-storage", "v1-rev20241008-2.0.0"),
    maven.artifact("com.google.auth", "google-auth-library-credentials", "1.30.0"),
    maven.artifact("com.google.auth", "google-auth-library-oauth2-http", "1.30.0"),
    maven.artifact("com.google.auto.value", "auto-value", "1.11.0"),
    maven.artifact("com.google.auto.value", "auto-value-annotations", "1.11.0"),
    maven.artifact("com.google.cloud", "google-cloud-core", "2.48.0"),
    maven.artifact("com.google.cloud", "google-cloud-core-grpc", "2.48.0"),
    maven.artifact("com.google.cloud", "google-cloud-core-http", "2.48.0"),
    maven.artifact("com.google.cloud", "google-cloud-monitoring", "3.31.0"),
    maven.artifact("com.google.cloud", "google-cloud-storage", "2.45.0"),
    maven.artifact("com.google.cloud.opentelemetry", "detector-resources-support", "0.32.0"),
    maven.artifact("com.google.cloud.opentelemetry", "exporter-metrics", "0.31.0"),
    maven.artifact("com.google.cloud.opentelemetry", "shared-resourcemapping", "0.32.0"),
    maven.artifact("com.google.code.findbugs", "jsr305", "3.0.2"),
    maven.artifact("com.google.code.gson", "gson", "2.11.0"),
    maven.artifact("com.google.errorprone", "error_prone_annotations", "2.35.1"),
    maven.artifact("com.google.guava", "failureaccess", "1.0.2"),
    maven.artifact("com.google.guava", "guava", "33.3.1-jre"),
    maven.artifact("com.google.guava", "listenablefuture", "9999.0-empty-to-avoid-conflict-with-guava"),
    maven.artifact("com.google.http-client", "google-http-client", "1.45.0"),
    maven.artifact("com.google.http-client", "google-http-client-apache-v2", "1.45.0"),
    maven.artifact("com.google.http-client", "google-http-client-appengine", "1.45.0"),
    maven.artifact("com.google.http-client", "google-http-client-gson", "1.45.0"),
    maven.artifact("com.google.http-client", "google-http-client-jackson2", "1.45.0"),
    maven.artifact("com.google.j2objc", "j2objc-annotations", "3.0.0"),
    maven.artifact("com.google.jimfs", "jimfs", "1.3.0"),
    maven.artifact("com.google.oauth-client", "google-oauth-client", "1.36.0"),
    maven.artifact("com.google.protobuf", "protobuf-java", "4.29.0"),
    maven.artifact("com.google.protobuf", "protobuf-java-util", "4.29.0"),
    maven.artifact("com.google.protobuf", "protobuf-kotlin", "4.29.0"),
    maven.artifact("com.google.re2j", "re2j", "1.7"),
    maven.artifact("com.google.truth", "truth", "1.4.4"),
    maven.artifact("com.google.truth.extensions", "truth-java8-extension", "1.4.4"),
    maven.artifact("com.google.truth.extensions", "truth-liteproto-extension", "1.4.4"),
    maven.artifact("com.google.truth.extensions", "truth-proto-extension", "1.4.4"),
    maven.artifact("com.networknt", "json-schema-validator", "1.5.4"),
    maven.artifact("com.salesforce.servicelibs", "grpc-contrib", "0.8.1"),
    maven.artifact("com.salesforce.servicelibs", "jprotoc", "1.2.2"),
    maven.artifact("com.salesforce.servicelibs", "reactive-grpc-gencommon", "1.2.4"),
    maven.artifact("com.squareup.okhttp3", "logging-interceptor", "4.12.0"),
    maven.artifact("com.squareup.okhttp3", "mockwebserver", "4.12.0"),
    maven.artifact("com.squareup.okhttp3", "okhttp", "4.12.0"),
    maven.artifact("com.squareup.okio", "okio-jvm", "3.6.0"),
    maven.artifact("commons-codec", "commons-codec", "1.17.1"),
    maven.artifact("commons-io", "commons-io", "2.16.1"),
    maven.artifact("commons-logging", "commons-logging", "1.2"),
    maven.artifact("dev.failsafe", "failsafe", "3.3.2"),
    maven.artifact("io.grpc", "grpc-alts", "1.68.2"),
    maven.artifact("io.grpc", "grpc-api", "1.68.2"),
    maven.artifact("io.grpc", "grpc-auth", "1.68.2"),
    maven.artifact("io.grpc", "grpc-context", "1.68.2"),
    maven.artifact("io.grpc", "grpc-core", "1.68.2", exclusions = ["io.grpc:grpc-util"]),
    maven.artifact("io.grpc", "grpc-googleapis", "1.68.2"),
    maven.artifact("io.grpc", "grpc-grpclb", "1.68.2"),
    maven.artifact("io.grpc", "grpc-inprocess", "1.68.2"),
    maven.artifact("io.grpc", "grpc-kotlin-stub", "1.4.1"),
    maven.artifact("io.grpc", "grpc-netty-shaded", "1.68.2"),
    maven.artifact("io.grpc", "grpc-opentelemetry", "1.68.2"),
    maven.artifact("io.grpc", "grpc-protobuf", "1.68.2"),
    maven.artifact("io.grpc", "grpc-protobuf-lite", "1.68.2"),
    maven.artifact("io.grpc", "grpc-rls", "1.68.2"),
    maven.artifact("io.grpc", "grpc-s2a", "1.68.2"),
    maven.artifact("io.grpc", "grpc-services", "1.68.2"),
    maven.artifact("io.grpc", "grpc-stub", "1.68.2"),
    maven.artifact("io.grpc", "grpc-util", "1.68.2"),
    maven.artifact("io.grpc", "grpc-xds", "1.68.2"),
    maven.artifact("io.micrometer", "micrometer-commons", "1.14.2"),
    maven.artifact("io.micrometer", "micrometer-observation", "1.14.2"),
    maven.artifact("io.netty", "netty-buffer", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-codec", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-codec-dns", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-codec-http", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-codec-http2", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-codec-socks", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-common", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-handler", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-handler-proxy", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-resolver", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-resolver-dns", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-resolver-dns-classes-macos", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-resolver-dns-native-macos", "4.1.116.Final", classifier = "osx-x86_64"),
    maven.artifact("io.netty", "netty-tcnative-boringssl-static", "2.0.69.Final"),
    maven.artifact("io.netty", "netty-tcnative-classes", "2.0.69.Final"),
    maven.artifact("io.netty", "netty-transport", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-transport-classes-epoll", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-transport-classes-kqueue", "4.1.116.Final"),
    maven.artifact("io.netty", "netty-transport-native-epoll", "4.1.116.Final", classifier = "linux-x86_64"),
    maven.artifact("io.netty", "netty-transport-native-kqueue", "4.1.116.Final", classifier = "osx-x86_64"),
    maven.artifact("io.netty", "netty-transport-native-unix-common", "4.1.116.Final"),
    maven.artifact("io.opencensus", "opencensus-api", "0.31.1"),
    maven.artifact("io.opencensus", "opencensus-contrib-http-util", "0.31.1"),
    maven.artifact("io.opencensus", "opencensus-proto", "0.2.0"),
    maven.artifact("io.opentelemetry", "opentelemetry-api", "1.45.0"),
    maven.artifact("io.opentelemetry", "opentelemetry-api-incubator", "1.45.0-alpha"),
    maven.artifact("io.opentelemetry", "opentelemetry-context", "1.45.0"),
    maven.artifact("io.opentelemetry", "opentelemetry-sdk", "1.45.0"),
    maven.artifact("io.opentelemetry", "opentelemetry-sdk-common", "1.45.0"),
    maven.artifact("io.opentelemetry", "opentelemetry-sdk-extension-autoconfigure-spi", "1.45.0"),
    maven.artifact("io.opentelemetry", "opentelemetry-sdk-logs", "1.45.0"),
    maven.artifact("io.opentelemetry", "opentelemetry-sdk-metrics", "1.45.0"),
    maven.artifact("io.opentelemetry", "opentelemetry-sdk-trace", "1.45.0"),
    maven.artifact("io.opentelemetry.contrib", "opentelemetry-gcp-resources", "1.37.0-alpha"),
    maven.artifact("io.opentelemetry.semconv", "opentelemetry-semconv", "1.25.0-alpha"),
    maven.artifact("io.perfmark", "perfmark-api", "0.27.0"),
    maven.artifact("io.projectreactor", "reactor-core", "3.7.1"),
    maven.artifact("io.projectreactor.netty", "reactor-netty-core", "1.2.1"),
    maven.artifact("io.projectreactor.netty", "reactor-netty-http", "1.2.1"),
    maven.artifact("javax.annotation", "javax.annotation-api", "1.3.2"),
    maven.artifact("junit", "junit", "4.13.2"),
    maven.artifact("net.bytebuddy", "byte-buddy", "1.15.11"),
    maven.artifact("net.bytebuddy", "byte-buddy-agent", "1.15.11"),
    maven.artifact("net.java.dev.jna", "jna", "5.13.0"),
    maven.artifact("net.sf.jopt-simple", "jopt-simple", "4.9"),
    maven.artifact("org.antlr", "stringtemplate", "3.2.1"),
    maven.artifact("org.apache.commons", "commons-compress", "1.27.1"),
    maven.artifact("org.apache.commons", "commons-lang3", "3.17.0"),
    maven.artifact("org.apache.commons", "commons-text", "1.12.0"),
    maven.artifact("org.apache.httpcomponents", "httpclient", "4.5.13"),
    maven.artifact("org.apache.httpcomponents", "httpcore", "4.4.16"),
    maven.artifact("org.apache.tomcat", "annotations-api", "6.0.53"),
    maven.artifact("org.awaitility", "awaitility", "4.2.2"),
    maven.artifact("org.bouncycastle", "bcpkix-jdk18on", "1.76"),
    maven.artifact("org.bouncycastle", "bcprov-jdk18on", "1.76"),
    maven.artifact("org.bouncycastle", "bcutil-jdk18on", "1.76"),
    maven.artifact("org.checkerframework", "checker-qual", "3.47.0"),
    maven.artifact("org.codehaus.mojo", "animal-sniffer-annotations", "1.24"),
    maven.artifact("org.codehaus.woodstox", "stax2-api", "4.2.2"),
    maven.artifact("org.conscrypt", "conscrypt-openjdk-uber", "2.5.2"),
    maven.artifact("org.hamcrest", "hamcrest", "2.2"),
    maven.artifact("org.hamcrest", "hamcrest-core", "2.2"),
    maven.artifact("org.jetbrains", "annotations", "26.0.1"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-reflect", "2.1.0"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-stdlib", "2.1.0"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-stdlib-jdk7", "2.1.0"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "2.1.0"),
    maven.artifact("org.jetbrains.kotlinx", "kotlinx-coroutines-core-jvm", "1.10.1"),
    maven.artifact("org.jspecify", "jspecify", "1.0.0"),
    maven.artifact("org.locationtech.jts", "jts-core", "1.20.0"),
    maven.artifact("org.mockito", "mockito-core", "5.14.2"),
    maven.artifact("org.objenesis", "objenesis", "3.3"),
    maven.artifact("org.ow2.asm", "asm", "9.7.1"),
    maven.artifact("org.ow2.asm", "asm-analysis", "9.7.1"),
    maven.artifact("org.ow2.asm", "asm-commons", "9.7.1"),
    maven.artifact("org.ow2.asm", "asm-tree", "9.7.1"),
    maven.artifact("org.ow2.asm", "asm-util", "9.7.1"),
    maven.artifact("org.pitest", "pitest", "1.17.3"),
    maven.artifact("org.pitest", "pitest-aggregator", "1.17.3"),
    maven.artifact("org.pitest", "pitest-command-line", "1.17.3"),
    maven.artifact("org.pitest", "pitest-entry", "1.17.3"),
    maven.artifact("org.pitest", "pitest-html-report", "1.17.3"),
    maven.artifact("org.reactivestreams", "reactive-streams", "1.0.4"),
    maven.artifact("org.rnorth.duct-tape", "duct-tape", "1.0.8"),
    maven.artifact("org.slf4j", "slf4j-api", "2.0.16"),
    maven.artifact("org.springframework", "spring-aop", "6.2.1"),
    maven.artifact("org.springframework", "spring-beans", "6.2.1"),
    maven.artifact("org.springframework", "spring-context", "6.2.1"),
    maven.artifact("org.springframework", "spring-core", "6.2.1"),
    maven.artifact("org.springframework", "spring-expression", "6.2.1"),
    maven.artifact("org.springframework", "spring-jcl", "6.2.1"),
    maven.artifact("org.springframework", "spring-web", "6.2.1"),
    maven.artifact("org.springframework.boot", "spring-boot", "3.4.1"),
    maven.artifact("org.springframework.cloud", "spring-cloud-commons", "4.2.0"),
    maven.artifact("org.springframework.security", "spring-security-crypto", "6.4.2"),
    maven.artifact("org.testcontainers", "testcontainers", "1.20.4"),
    maven.artifact("org.threeten", "threetenbp", "1.7.0"),
    maven.artifact("org.yaml", "snakeyaml", "2.3"),
    maven.artifact("software.amazon.awssdk", "annotations", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "apache-client", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "arns", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "auth", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "aws-core", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "aws-query-protocol", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "aws-xml-protocol", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "checksums", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "checksums-spi", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "crt-core", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "endpoints-spi", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "http-auth", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "http-auth-aws", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "http-auth-aws-eventstream", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "http-auth-spi", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "http-client-spi", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "identity-spi", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "json-utils", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "metrics-spi", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "netty-nio-client", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "profiles", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "protocol-core", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "regions", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "retries", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "retries-spi", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "s3", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "sdk-core", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "third-party-jackson-core", "2.29.43"),
    maven.artifact("software.amazon.awssdk", "utils", "2.29.43"),
    maven.artifact("software.amazon.eventstream", "eventstream", "1.0.1"),
]

EXCLUSIONS = []
