load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@rules_java//java:defs.bzl", "java_library")

maven_library(
    name = "file",
    artifact_name = "libeopp :: file",
    protos = [{
        "name": "file",
        "srcs": "src/main/proto/file.proto",
        "deps": [
            "@com_google_protobuf//:any_proto",
            "@com_google_protobuf//:timestamp_proto",
        ],
    }],
    root_packages = ["com.cgi.eoss.eopp.file"],
    test_suite = [":file_tests"],
    deps = [
        "//third_party/java:org_springframework_spring_core_neverlink",
        "//util:eopp-headers",
        "//util:timestamps",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_protobuf_protobuf_java",
    ],
)

java_library(
    name = "file_test_resources",
    resources = glob(["src/test/resources/**/*"]),
)

gen_java_tests(
    name = "file_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":file",
        ":file_test_resources",
        "//util:eopp-headers",
        "//util:timestamps",
        "@com_google_protobuf//:any_proto",
        "@com_google_protobuf//:protobuf_java",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_jimfs_jimfs",
        "@maven//:com_google_truth_extensions_truth_java8_extension",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
