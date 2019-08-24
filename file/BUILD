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
    ],
)

java_proto_library(
    name = "file_java_proto",
    visibility = ["//visibility:public"],
    deps = [":file_proto"],
)

load("//tools:maven.bzl", "maven_library")

maven_library(
    name = "file",
    srcs = [":file_java_srcs"],
    artifact_name = "libeopp :: file",
    exports = [":file_java_proto"],
    deps = [
        ":file_java_proto",
        "//third_party/protobuf:protobuf_java",
        "@com_google_guava_guava",
    ],
)

load("@google_bazel_common//tools/jarjar:jarjar.bzl", "jarjar_library")

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
    visibility = ["//:__pkg__"],
)

java_library(
    name = "file_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    data = glob(["src/test/resources/**"]),
    deps = [
        ":file",
        "@com_google_guava_guava",
        "@com_google_protobuf//:any_proto",
        "@com_google_protobuf//:protobuf_java",
        "@com_google_truth_extensions_truth_liteproto_extension",
        "@com_google_truth_extensions_truth_proto_extension",
        "@com_google_truth_truth",
        "@junit_junit",
    ],
)

java_test(
    name = "FileMetasTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.file.FileMetasTest",
    runtime_deps = [":file_java_tests"],
)
