load("@rules_jvm_external//:specs.bzl", "maven")

REPOSITORIES = [
    "https://jcenter.bintray.com/",
]

ARTIFACTS = [
    maven.artifact("ch.qos.logback", "logback-classic", "1.2.3"),
    maven.artifact("ch.qos.logback", "logback-core", "1.2.3"),
    maven.artifact("com.fasterxml.jackson.core", "jackson-annotations", "2.11.0"),
    maven.artifact("com.fasterxml.jackson.core", "jackson-core", "2.11.0"),
    maven.artifact("com.fasterxml.jackson.core", "jackson-databind", "2.11.0"),
    maven.artifact("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml", "2.11.0"),
    maven.artifact("com.github.spullara.mustache.java", "compiler", "0.9.4"),
    maven.artifact("com.google.android", "annotations", "4.1.1.4"),
    maven.artifact("com.google.api.grpc", "proto-google-common-protos", "1.18.0"),
    maven.artifact("com.google.auto.value", "auto-value-annotations", "1.6.3", neverlink = True),
    maven.artifact("com.google.code.findbugs", "jsr305", "3.0.2"),
    maven.artifact("com.google.code.gson", "gson", "2.8.6"),
    maven.artifact("com.google.errorprone", "error_prone_annotations", "2.3.4", neverlink = True),
    maven.artifact("com.google.guava", "failureaccess", "1.0.1"),
    maven.artifact("com.google.guava", "guava", "29.0-jre"),
    maven.artifact("com.google.guava", "listenablefuture", "9999.0-empty-to-avoid-conflict-with-guava"),
    maven.artifact("com.google.j2objc", "j2objc-annotations", "1.3"),
    maven.artifact("com.google.protobuf", "protobuf-java", "3.12.2"),
    maven.artifact("com.google.protobuf", "protobuf-java-util", "3.12.2"),
    maven.artifact("com.google.protobuf", "protobuf-javalite", "3.12.2"),
    maven.artifact("com.google.truth.extensions", "truth-java8-extension", "1.0.1", testonly = True),
    maven.artifact("com.google.truth.extensions", "truth-liteproto-extension", "1.0.1", testonly = True),
    maven.artifact("com.google.truth.extensions", "truth-proto-extension", "1.0.1", testonly = True),
    maven.artifact("com.google.truth", "truth", "1.0.1", testonly = True),
    maven.artifact("com.googlecode.java-diff-utils", "diffutils", "1.3.0"),
    maven.artifact("com.salesforce.servicelibs", "grpc-contrib", "0.8.1"),
    maven.artifact("com.salesforce.servicelibs", "jprotoc", "0.9.1"),
    maven.artifact("com.salesforce.servicelibs", "reactive-grpc-gencommon", "1.0.1"),
    maven.artifact("com.squareup.okhttp3", "logging-interceptor", "4.7.2"),
    maven.artifact("com.squareup.okhttp3", "mockwebserver", "4.7.2", testonly = True),
    maven.artifact("com.squareup.okhttp3", "okhttp", "4.7.2"),
    maven.artifact("com.squareup.okio", "okio", "2.6.0"),
    maven.artifact("com.typesafe.netty", "netty-reactive-streams", "2.0.4"),
    maven.artifact("com.typesafe.netty", "netty-reactive-streams-http", "2.0.4"),
    maven.artifact("commons-codec", "commons-codec", "1.14"),
    maven.artifact("io.grpc", "grpc-api", "1.30.0"),
    maven.artifact("io.grpc", "grpc-context", "1.30.0"),
    maven.artifact("io.grpc", "grpc-core", "1.30.0"),
    maven.artifact("io.grpc", "grpc-netty", "1.30.0"),
    maven.artifact("io.grpc", "grpc-protobuf", "1.30.0"),
    maven.artifact("io.grpc", "grpc-protobuf-lite", "1.30.0"),
    maven.artifact("io.grpc", "grpc-services", "1.30.0"),
    maven.artifact("io.grpc", "grpc-stub", "1.30.0"),
    maven.artifact("io.netty", "netty-buffer", "4.1.50.Final"),
    maven.artifact("io.netty", "netty-codec", "4.1.50.Final"),
    maven.artifact("io.netty", "netty-codec-http", "4.1.50.Final"),
    maven.artifact("io.netty", "netty-codec-http2", "4.1.50.Final"),
    maven.artifact("io.netty", "netty-codec-socks", "4.1.50.Final"),
    maven.artifact("io.netty", "netty-common", "4.1.50.Final"),
    maven.artifact("io.netty", "netty-handler", "4.1.50.Final"),
    maven.artifact("io.netty", "netty-handler-proxy", "4.1.50.Final"),
    maven.artifact("io.netty", "netty-resolver", "4.1.50.Final"),
    maven.artifact("io.netty", "netty-transport", "4.1.50.Final"),
    maven.artifact("io.netty", "netty-transport-native-epoll", "4.1.50.Final", classifier = "linux-x86_64"),
    maven.artifact("io.netty", "netty-transport-native-unix-common", "4.1.50.Final"),
    maven.artifact("io.perfmark", "perfmark-api", "0.19.0"),
    maven.artifact("io.projectreactor", "reactor-core", "3.3.6.RELEASE"),
    maven.artifact("javax.annotation", "javax.annotation-api", "1.3.2", neverlink = True),
    maven.artifact("junit", "junit", "4.13", testonly = True),
    maven.artifact("net.bytebuddy", "byte-buddy", "1.10.11"),
    maven.artifact("net.bytebuddy", "byte-buddy-agent", "1.10.11"),
    maven.artifact("org.apache.httpcomponents", "httpclient", "4.5.12"),
    maven.artifact("org.apache.httpcomponents", "httpcore", "4.4.13"),
    maven.artifact("org.apache.tomcat", "annotations-api", "6.0.53", neverlink = True),
    maven.artifact("org.checkerframework", "checker-compat-qual", "2.5.5"),
    maven.artifact("org.checkerframework", "checker-qual", "2.11.1"),
    maven.artifact("org.codehaus.mojo", "animal-sniffer-annotations", "1.18", neverlink = True),
    maven.artifact("org.hamcrest", "hamcrest", "2.2", testonly = True),
    maven.artifact("org.hamcrest", "hamcrest-core", "2.2", testonly = True),
    maven.artifact("org.jetbrains", "annotations", "13.0", neverlink = True),
    maven.artifact("org.jetbrains.kotlin", "kotlin-reflect", "1.3.72"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-stdlib", "1.3.72"),
    maven.artifact("org.jetbrains.kotlin", "kotlin-stdlib-common", "1.3.72"),
    maven.artifact("org.mockito", "mockito-core", "3.3.3", testonly = True),
    maven.artifact("org.objenesis", "objenesis", "2.6"),
    maven.artifact("org.reactivestreams", "reactive-streams", "1.0.3"),
    maven.artifact("org.slf4j", "slf4j-api", "1.7.30"),
    maven.artifact("org.springframework.cloud", "spring-cloud-commons", "2.2.3.RELEASE"),
    maven.artifact("org.springframework.security", "spring-security-crypto", "5.3.3.RELEASE"),
    maven.artifact("org.springframework", "spring-beans", "5.2.7.RELEASE"),
    maven.artifact("org.springframework", "spring-core", "5.2.7.RELEASE"),
    maven.artifact("org.springframework", "spring-jcl", "5.2.7.RELEASE"),
    maven.artifact("org.springframework", "spring-web", "5.2.7.RELEASE"),
    maven.artifact("org.yaml", "snakeyaml", "1.26"),
    maven.artifact("software.amazon.awssdk", "annotations", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "apache-client", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "arns", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "auth", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "aws-core", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "aws-query-protocol", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "aws-xml-protocol", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "http-client-spi", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "netty-nio-client", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "profiles", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "protocol-core", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "regions", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "s3", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "sdk-core", "2.13.30"),
    maven.artifact("software.amazon.awssdk", "utils", "2.13.30"),
    maven.artifact("software.amazon.eventstream", "eventstream", "1.0.1"),
]
