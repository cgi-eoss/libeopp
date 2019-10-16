filegroup(
    name = "job-graph_java_srcs",
    srcs = glob([
        "src/main/java/**/*.java",
        "src/main/java/**/*.kt",
    ]),
)

load("//tools:maven.bzl", "maven_coordinates_tag", "maven_library")

maven_library(
    name = "job-graph",
    srcs = [":job-graph_java_srcs"],
    artifact_name = "libeopp :: job-graph",
    build_javadoc_library = False,
    build_kt_jvm_library = True,
    runtime_deps = ["@org_jetbrains_kotlin_kotlin_reflect"],
    deps = [
        "//job",
        "//identifier",
        "//workflow",
        "@com_google_guava_guava",
        "@org_slf4j_slf4j_api",
    ],
)

java_library(
    name = "job-graph_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    runtime_deps = [
        "@ch_qos_logback_logback_classic",
    ],
    deps = [
        ":job-graph",
        "//job",
        "//identifier",
        "//workflow",
        "@com_google_truth_extensions_truth_proto_extension",
        "@com_google_truth_truth",
        "@junit_junit",
    ],
)

java_test(
    name = "JobGraphTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.jobgraph.JobGraphTest",
    runtime_deps = [":job-graph_java_tests"],
)
