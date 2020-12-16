load("//tools:maven.bzl", "maven_library")
load("@rules_java//java:defs.bzl", "java_library", "java_test")

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
    root_packages = ["com.cgi.eoss.eopp.jobgraph"],
    test_targets = [
        ":JobGraphTest",
        ":StepInstanceExpanderNestedWorkflowTest",
        ":StepInstanceExpanderParallelTest",
    ],
    runtime_deps = ["@org_jetbrains_kotlin_kotlin_reflect"],
    deps = [
        "//identifier",
        "//job",
        "//workflow",
        "@maven//:com_google_guava_guava",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_spring_core",
    ],
)

java_library(
    name = "job-graph_java_tests",
    testonly = True,
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

java_test(
    name = "JobGraphTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.jobgraph.JobGraphTest",
    runtime_deps = [":job-graph_java_tests"],
)

java_test(
    name = "StepInstanceExpanderNestedWorkflowTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.jobgraph.StepInstanceExpanderNestedWorkflowTest",
    runtime_deps = [":job-graph_java_tests"],
)

java_test(
    name = "StepInstanceExpanderParallelTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.jobgraph.StepInstanceExpanderParallelTest",
    runtime_deps = [":job-graph_java_tests"],
)
