load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@rules_java//java:defs.bzl", "java_library", "java_test")

filegroup(
    name = "job-executor_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

maven_library(
    name = "job-executor",
    srcs = [":job-executor_java_srcs"],
    artifact_name = "libeopp :: job-executor",
    root_packages = ["com.cgi.eoss.eopp.executor"],
    test_suite = [":job-executor_tests"],
    deps = [
        "//job",
        "//job-graph",
        "//workflow",
        "@maven//:com_google_guava_guava",
        "@maven//:io_projectreactor_reactor_core",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_spring_core",
    ],
)

gen_java_tests(
    name = "job-executor_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    runtime_deps = [
        "@maven//:ch_qos_logback_logback_classic",
    ],
    deps = [
        ":job-executor",
        "//identifier",
        "//job",
        "//workflow",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
