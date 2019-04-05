licenses(["notice"])

# Re-exports with Maven coordinates for POM generation

VERSION = "1.19.0"

java_library(
    name = "context",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-context:" + VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//context"],
)

# Several targets are part of io.grpc:grpc-core, so tag them the same

java_library(
    name = "core",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-core:" + VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//core"],
)

java_library(
    name = "core_inprocess",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-core:" + VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//core:inprocess"],
)

java_library(
    name = "core_util",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-core:" + VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//core:util"],
)

java_library(
    name = "netty",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-netty:" + VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//netty"],
)

java_library(
    name = "protobuf",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-protobuf:" + VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//protobuf"],
)

java_library(
    name = "stub",
    tags = [
        "manual",
        "maven_coordinates=io.grpc:grpc-stub:" + VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@io_grpc_grpc_java//stub"],
)
