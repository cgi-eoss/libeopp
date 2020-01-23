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
    deps = [
        ":workflow_java_proto",
        "//third_party/protobuf:protobuf_java",
        "//third_party/protobuf:protobuf_java_util",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:com_fasterxml_jackson_dataformat_jackson_dataformat_yaml",
    ],
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

java_library(
    name = "workflow_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    resources = glob(["src/test/resources/**"]),
    runtime_deps = [
        "@maven//:ch_qos_logback_logback_classic",
    ],
    deps = [
        ":workflow",
        "//identifier",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)

java_test(
    name = "WorkflowsTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.workflow.WorkflowsTest",
    runtime_deps = [":workflow_java_tests"],
)
