load("//tools:maven.bzl", "maven_coordinates_tag", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@google_bazel_common//tools/jarjar:jarjar.bzl", "jarjar_library")
load("@rules_java//java:defs.bzl", "java_library", "java_proto_library", "java_test")
load("@rules_proto//proto:defs.bzl", "proto_library")

filegroup(
    name = "file_proto_srcs",
    srcs = glob(["src/main/proto/**/*.proto"]),
)

filegroup(
    name = "file_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

proto_library(
    name = "file_proto",
    srcs = [":file_proto_srcs"],
    visibility = ["//visibility:public"],
    deps = [
        "@com_google_protobuf//:any_proto",
        "@com_google_protobuf//:timestamp_proto",
    ],
)

java_proto_library(
    name = "file_java_proto",
    visibility = ["//visibility:public"],
    deps = [":file_proto"],
)

maven_library(
    name = "file",
    srcs = [":file_java_srcs"],
    artifact_name = "libeopp :: file",
    deploy_java_library = False,
    root_packages = ["com.cgi.eoss.eopp.file"],
    test_suite = [":file_tests"],
    exports = [":file_java_proto"],
    deps = [
        ":file_java_proto",
        "//third_party/protobuf:protobuf_java",
        "//util:eopp-headers",
        "//util:timestamps",
        "@maven//:com_google_guava_guava",
    ],
)

# Bundle the proto-derived classes for the Maven artifact
jarjar_library(
    name = "libfile_deploy",
    jars = [
        ":file",
        ":file_java_proto",
    ],
    rules = [
        "zap com.google.protobuf.**",
    ],
    tags = [
        "maven_artifact",
        maven_coordinates_tag("file"),
    ],
    visibility = ["//:__pkg__"],
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
        "//util:timestamps",
        "@com_google_protobuf//:any_proto",
        "@com_google_protobuf//:protobuf_java",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:com_google_truth_extensions_truth_java8_extension",
        "@maven//:junit_junit",
    ],
)
