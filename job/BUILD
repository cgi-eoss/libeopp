load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")

maven_library(
    name = "job",
    artifact_name = "libeopp :: job",
    generate_pitest_coverage_target = True,
    protos = [{
        "name": "job",
        "srcs": "src/main/proto/job.proto",
        "deps": [
            "//identifier:identifier_proto",
            "//workflow:workflow_proto",
            "@com_google_protobuf//:any_proto",
            "@com_google_protobuf//:timestamp_proto",
        ],
    }],
    root_packages = ["com.cgi.eoss.eopp.job"],
    test_suite = [":job_tests"],
    deps = [
        "//third_party/java:org_springframework_spring_core_neverlink",
        "//workflow",
        "@maven//:com_google_guava_guava",
        "@maven//:org_springframework_spring_web",
    ],
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
