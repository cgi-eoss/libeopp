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

load("//tools:maven.bzl", "maven_coordinates_tag", "maven_library")

maven_library(
    name = "file",
    srcs = [":file_java_srcs"],
    artifact_name = "libeopp :: file",
    deploy_java_library = False,
    root_packages = ["com.cgi.eoss.eopp.file"],
    test_targets = [
        ":FileMetasTest",
    ],
    exports = [":file_java_proto"],
    deps = [
        ":file_java_proto",
        "//third_party/protobuf:protobuf_java",
        "//util:eopp-headers",
        "//util:timestamps",
        "@maven//:com_google_guava_guava",
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
    tags = [
        "maven_artifact",
        maven_coordinates_tag("file"),
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
        "//util:timestamps",
        "@com_google_protobuf//:any_proto",
        "@com_google_protobuf//:protobuf_java",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)

java_test(
    name = "FileMetasTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.file.FileMetasTest",
    runtime_deps = [":file_java_tests"],
)
