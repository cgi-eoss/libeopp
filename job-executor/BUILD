load("@rules_java//java:defs.bzl", "java_library", "java_test")
load("//tools:maven.bzl", "maven_library")

filegroup(
    name = "job-executor_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

maven_library(
    name = "job-executor",
    srcs = [":job-executor_java_srcs"],
    artifact_name = "libeopp :: job-executor",
    root_packages = ["com.cgi.eoss.eopp.executor"],
    test_targets = [
        ":AbstractStepOperatorTest",
    ],
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

java_library(
    name = "job-executor_java_tests",
    testonly = True,
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

java_test(
    name = "AbstractStepOperatorTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.executor.AbstractStepOperatorTest",
    runtime_deps = [":job-executor_java_tests"],
)
