load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")

filegroup(
    name = "job-graph_java_srcs",
    srcs = glob([
        "src/main/java/**/*.java",
        "src/main/java/**/*.kt",
    ]),
)

maven_library(
    name = "job-graph",
    srcs = [":job-graph_java_srcs"],
    artifact_name = "libeopp :: job-graph",
    build_javadoc_library = False,
    build_kt_jvm_library = True,
    generate_pitest_coverage_target = True,
    root_packages = ["com.cgi.eoss.eopp.jobgraph"],
    test_suite = [":job-graph_tests"],
    runtime_deps = ["@org_jetbrains_kotlin_kotlin_reflect"],
    deps = [
        "//identifier",
        "//job",
        "//third_party/java:org_springframework_spring_core_neverlink",
        "//workflow",
        "@maven//:com_google_guava_guava",
        "@maven//:net_jodah_failsafe",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_spring_core",
    ],
)

gen_java_tests(
    name = "job-graph_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    runtime_deps = [
        "@maven//:ch_qos_logback_logback_classic",
    ],
    deps = [
        ":job-graph",
        "//identifier",
        "//job:job_java_proto",
        "//workflow:workflow_java_proto",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
