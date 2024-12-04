load("@com_google_protobuf//bazel:java_proto_library.bzl", "java_proto_library")
load("@com_google_protobuf//bazel:proto_library.bzl", "proto_library")
load("@com_salesforce_servicelibs_reactive_grpc//bazel:java_reactive_grpc_library.bzl", "reactor_grpc_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")
load("@rules_java//java:defs.bzl", "java_library")
load("//tools:maven.bzl", "maven_library")

maven_library(
    name = "file-stream",
    artifact_name = "libeopp :: file-stream",
    generate_pitest_coverage_target = True,
    root_packages = ["com.cgi.eoss.eopp.filestream"],
    test_suite = [":file-stream_tests"],
    deps = [
        "//file",
        "//resource",
        "//rpc",
        "//third_party/grpc:core",
        "//third_party/grpc:reactor_grpc_stub",
        "//third_party/grpc:stub",
        "//third_party/java:org_springframework_spring_core_neverlink",
        "//third_party/protobuf:protobuf_java",
        "@maven//:com_google_guava_guava",
        "@maven//:io_projectreactor_reactor_core",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_spring_core",
    ],
)

java_library(
    name = "file-stream_test_resources",
    resources = glob(["src/test/resources/**/*"]),
)

gen_java_tests(
    name = "file-stream_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":file-stream",
        ":file-stream_test_resources",
        ":filestream_test_server_java",
        "//file",
        "//rpc",
        "//third_party/grpc:api",
        "//third_party/grpc:core_inprocess",
        "//third_party/grpc:stub",
        "//third_party/grpc:testing",
        "//util:io",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:io_projectreactor_reactor_core",
        "@maven//:junit_junit",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_spring_core",
    ],
)

proto_library(
    name = "filestream_test_server_proto",
    testonly = True,
    srcs = ["src/test/protobuf/filestream_test_server.proto"],
    deps = ["//file:file_proto"],
)

java_proto_library(
    name = "filestream_test_server_java_proto",
    testonly = True,
    visibility = ["//visibility:public"],
    deps = [":filestream_test_server_proto"],
)

java_grpc_library(
    name = "filestream_test_server_java_grpc",
    testonly = True,
    srcs = [":filestream_test_server_proto"],
    visibility = ["//visibility:public"],
    deps = [":filestream_test_server_java_proto"],
)

reactor_grpc_library(
    name = "filestream_test_server_reactor_grpc",
    testonly = True,
    proto = ":filestream_test_server_proto",
    visibility = ["//visibility:public"],
    deps = [":filestream_test_server_java_grpc"],
)

java_library(
    name = "filestream_test_server_java",
    testonly = True,
    srcs = [
        "src/test/java/com/cgi/eoss/eopp/filestream/FileStreamTestServer.java",
        "src/test/java/com/cgi/eoss/eopp/filestream/ReactiveFileStreamTestServer.java",
    ],
    visibility = ["//visibility:public"],
    exports = [
        ":filestream_test_server_java_grpc",
        ":filestream_test_server_java_proto",
        ":filestream_test_server_reactor_grpc",
    ],
    runtime_deps = [
        "@maven//:ch_qos_logback_logback_classic",
    ],
    deps = [
        ":filestream_test_server_java_grpc",
        ":filestream_test_server_java_proto",
        ":filestream_test_server_reactor_grpc",
        "//file",
        "//file-stream",
        "//third_party/grpc:api",
        "//third_party/grpc:stub",
        "@maven//:com_google_guava_guava",
        "@maven//:io_projectreactor_reactor_core",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_spring_core",
    ],
)
