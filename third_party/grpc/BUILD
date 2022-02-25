load("@rules_java//java:defs.bzl", "java_library")
load(":grpc_repositories.bzl", "GRPC_JAVA_VERSION", "REACTOR_GRPC_VERSION")

licenses(["notice"])

# Re-exports with Maven coordinates for POM generation

java_library(
    name = "alts",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-alts:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//alts"],
)

java_library(
    name = "api",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-api:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//api"],
)

java_library(
    name = "auth",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-auth:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//auth"],
)

java_library(
    name = "census",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-census:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//census"],
)

java_library(
    name = "context",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-context:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//context"],
)

# Several targets are part of io.grpc:grpc-core, so tag them the same

java_library(
    name = "core",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-core:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//core:core_maven"],
)

java_library(
    name = "core_inprocess",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-core:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//core:inprocess"],
)

java_library(
    name = "core_util",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-core:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//core:util"],
)

java_library(
    name = "grpclb",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-grpclb:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//grpclb"],
)

java_library(
    name = "netty",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-netty:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//netty"],
)

java_library(
    name = "netty-shaded",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-netty-shaded:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//netty:shaded_maven"],
)

java_library(
    name = "protobuf",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-protobuf:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//protobuf"],
)

java_library(
    name = "protobuf_lite",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-protobuf-lite:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//protobuf-lite"],
)

java_library(
    name = "all_services",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-services:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = [
        "@io_grpc_grpc_java//services:binarylog",
        "@io_grpc_grpc_java//services:channelz",
        "@io_grpc_grpc_java//services:health",
        "@io_grpc_grpc_java//services:reflection",
    ],
)

java_library(
    name = "services_binarylog",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-services:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//services:binarylog"],
)

java_library(
    name = "services_channelz",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-services:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//services:channelz"],
)

java_library(
    name = "services_health",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-services:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//services:health"],
)

java_library(
    name = "services_reflection",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-services:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//services:reflection"],
)

java_library(
    name = "stub",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-stub:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//stub"],
)

java_library(
    name = "testing",
    testonly = True,
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-testing:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//testing"],
)

java_library(
    name = "reactor_grpc_stub",
    tags = [
        "manual",
        "maven_coordinates=com.salesforce.servicelibs:reactor-grpc-stub:" + REACTOR_GRPC_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@com_salesforce_servicelibs_reactive_grpc//reactor/reactor-grpc-stub"],
)
