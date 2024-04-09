load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")

maven_library(
    name = "job-executor",
    artifact_name = "libeopp :: job-executor",
    generate_pitest_coverage_target = True,
    root_packages = ["com.cgi.eoss.eopp.executor"],
    test_suite = [":job-executor_tests"],
    deps = [
        "//job",
        "//job-graph",
        "//third_party/java:org_springframework_spring_core_neverlink",
        "//workflow",
        "@maven//:com_google_guava_guava",
        "@maven//:dev_failsafe_failsafe",
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
        "@maven//:dev_failsafe_failsafe",
        "@maven//:junit_junit",
        "@maven//:org_awaitility_awaitility",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
