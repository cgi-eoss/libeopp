filegroup(
    name = "file_proto_srcs",
    srcs = glob(["src/main/proto/**/*.proto"]),
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
    name = "file_proto_java",
    visibility = ["//visibility:public"],
    deps = [":file_proto"],
)

filegroup(
    name = "file_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

java_library(
    name = "file",
    srcs = [":file_java_srcs"],
    visibility = ["//visibility:public"],
    exports = [":file_proto_java"],
    deps = [
        ":file_proto_java",
        "@com_google_guava_guava",
        "@com_google_protobuf//:protobuf_java",
    ],
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
