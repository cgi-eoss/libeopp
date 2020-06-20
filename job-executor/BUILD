filegroup(
    name = "job-executor_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

load("//tools:maven.bzl", "maven_coordinates_tag", "maven_library")

maven_library(
    name = "job-executor",
    srcs = [":job-executor_java_srcs"],
    artifact_name = "libeopp :: job-executor",
    deps = [
        "//job",
        "//job-graph",
        "//workflow",
        "@maven//:com_google_guava_guava",
        "@maven//:io_projectreactor_reactor_core",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_spring_core",
    ],
    root_packages = ["com.cgi.eoss.eopp.executor"],
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
