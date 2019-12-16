licenses(["notice"])

# Re-exports with Maven coordinates for POM generation

load(":grpc_repositories.bzl", "GRPC_JAVA_VERSION")

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
    exports = ["@io_grpc_grpc_java//core"],
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
    name = "netty",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-netty:" + GRPC_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//netty"],
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
