filegroup(
    name = "workflow_proto_srcs",
    srcs = glob(["src/main/proto/**/*.proto"]),
)

filegroup(
    name = "workflow_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

proto_library(
    name = "workflow_proto",
    srcs = [":workflow_proto_srcs"],
    visibility = ["//visibility:public"],
    deps = ["//identifier:identifier_proto"],
)

java_proto_library(
    name = "workflow_java_proto",
    visibility = ["//visibility:public"],
    deps = [":workflow_proto"],
)

load("//tools:maven.bzl", "maven_coordinates_tag", "maven_library")

maven_library(
    name = "workflow",
    srcs = [":workflow_java_srcs"],
    artifact_name = "libeopp :: workflow",
    deploy_java_library = False,
    exports = [":workflow_java_proto"],
    deps = [":workflow_java_proto"],
)

load("@google_bazel_common//tools/jarjar:jarjar.bzl", "jarjar_library")

# Bundle the proto-derived classes for the Maven artifact
jarjar_library(
    name = "libworkflow_deploy",
    jars = [
        ":workflow",
        ":workflow_java_proto",
    ],
    rules = [
        "zap com.google.protobuf.**",
    ],
    tags = [
        "maven_artifact",
        maven_coordinates_tag("workflow"),
    ],
    visibility = ["//:__pkg__"],
)
