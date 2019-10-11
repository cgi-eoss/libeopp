filegroup(
    name = "job_proto_srcs",
    srcs = glob(["src/main/proto/**/*.proto"]),
)

filegroup(
    name = "job_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

proto_library(
    name = "job_proto",
    srcs = [":job_proto_srcs"],
    visibility = ["//visibility:public"],
    deps = [
        "//identifier:identifier_proto",
        "//workflow:workflow_proto",
        "@com_google_protobuf//:any_proto",
        "@com_google_protobuf//:timestamp_proto",
    ],
)

java_proto_library(
    name = "job_java_proto",
    visibility = ["//visibility:public"],
    deps = [":job_proto"],
)

load("//tools:maven.bzl", "maven_coordinates_tag", "maven_library")

maven_library(
    name = "job",
    srcs = [":job_java_srcs"],
    artifact_name = "libeopp :: job",
    deploy_java_library = False,
    exports = [":job_java_proto"],
    deps = [
        ":job_java_proto",
        "@com_google_guava_guava",
    ],
)

load("@google_bazel_common//tools/jarjar:jarjar.bzl", "jarjar_library")

# Bundle the proto-derived classes for the Maven artifact
jarjar_library(
    name = "libjob_deploy",
    jars = [
        ":job",
        ":job_java_proto",
    ],
    rules = [
        "zap com.google.protobuf.**",
    ],
    tags = [
        "maven_artifact",
        maven_coordinates_tag("job"),
    ],
    visibility = ["//:__pkg__"],
)
