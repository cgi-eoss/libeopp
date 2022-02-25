load("//tools:maven.bzl", "maven_coordinates_tag", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@google_bazel_common//tools/jarjar:jarjar.bzl", "jarjar_library")
load("@rules_java//java:defs.bzl", "java_library", "java_proto_library", "java_test")
load("@rules_proto//proto:defs.bzl", "proto_library")

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

maven_library(
    name = "job",
    srcs = [":job_java_srcs"],
    artifact_name = "libeopp :: job",
    deploy_java_library = False,
    generate_pitest_coverage_target = True,
    root_packages = ["com.cgi.eoss.eopp.job"],
    test_suite = [":job_tests"],
    exports = [":job_java_proto"],
    deps = [
        ":job_java_proto",
        "//workflow",
        "@maven//:com_google_guava_guava",
        "@maven//:org_springframework_spring_web",
    ],
)

# Bundle the proto-derived classes for the Maven artifact
jarjar_library(
    name = "job_deploy",
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

gen_java_tests(
    name = "job_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    runtime_deps = [
        "@maven//:ch_qos_logback_logback_classic",
    ],
    deps = [
        ":job",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
