load("//tools:maven.bzl", "maven_coordinates_tag", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@google_bazel_common//tools/jarjar:jarjar.bzl", "jarjar_library")
load("@rules_java//java:defs.bzl", "java_library", "java_proto_library", "java_test")
load("@rules_proto//proto:defs.bzl", "proto_library")

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

maven_library(
    name = "workflow",
    srcs = [":workflow_java_srcs"],
    artifact_name = "libeopp :: workflow",
    deploy_java_library = False,
    root_packages = ["com.cgi.eoss.eopp.workflow"],
    test_suite = [":workflow_tests"],
    exports = [":workflow_java_proto"],
    deps = [
        ":workflow_java_proto",
        "//third_party/protobuf:protobuf_java",
        "//third_party/protobuf:protobuf_java_util",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:com_fasterxml_jackson_dataformat_jackson_dataformat_yaml",
    ],
)

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

java_library(
    name = "workflow_test_resources",
    resources = glob(["src/test/resources/**/*"]),
)

gen_java_tests(
    name = "workflow_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":workflow",
        ":workflow_test_resources",
        "//identifier",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
