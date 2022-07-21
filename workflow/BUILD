load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@rules_java//java:defs.bzl", "java_library")

maven_library(
    name = "workflow",
    artifact_name = "libeopp :: workflow",
    generate_pitest_coverage_target = True,
    protos = [{
        "name": "workflow",
        "srcs": "src/main/proto/workflow.proto",
        "deps": [
            "//identifier:identifier_proto",
        ],
    }],
    root_packages = ["com.cgi.eoss.eopp.workflow"],
    test_suite = [":workflow_tests"],
    deps = [
        "//third_party/java:org_springframework_spring_core_neverlink",
        "//third_party/protobuf:protobuf_java",
        "//third_party/protobuf:protobuf_java_util",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:com_fasterxml_jackson_dataformat_jackson_dataformat_yaml",
    ],
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
