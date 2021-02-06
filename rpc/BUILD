load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")
load("@rules_java//java:defs.bzl", "java_library", "java_proto_library", "java_test")
load("@rules_proto//proto:defs.bzl", "proto_library")

filegroup(
    name = "rpc_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

filegroup(
    name = "rpc_java_resources",
    srcs = glob(["src/main/resources/**"]),
)

maven_library(
    name = "rpc",
    srcs = [":rpc_java_srcs"],
    artifact_name = "libeopp :: rpc",
    root_packages = ["com.cgi.eoss.eopp.rpc"],
    test_suite = [":rpc_tests"],
    deps = [
        "//third_party/grpc:core",
        "//third_party/grpc:core_inprocess",
        "//third_party/grpc:stub",
        "//third_party/protobuf:protobuf_java",
        "//util",
        "@maven//:com_google_code_findbugs_jsr305",
        "@maven//:com_google_guava_guava",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_cloud_spring_cloud_commons",
        "@maven//:org_springframework_spring_core",
    ],
)

gen_java_tests(
    name = "rpc_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":helloworld_java_grpc",
        ":helloworld_java_proto",
        ":rpc",
        "@io_grpc_grpc_java//core",
        "@io_grpc_grpc_java//core:inprocess",
        "@io_grpc_grpc_java//netty",
        "@io_grpc_grpc_java//stub",
        "@io_grpc_grpc_java//testing",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
        "@maven//:org_mockito_mockito_core",
        "@maven//:org_springframework_cloud_spring_cloud_commons",
    ],
)

proto_library(
    name = "helloworld_proto",
    srcs = ["src/test/proto/helloworld.proto"],
)

java_proto_library(
    name = "helloworld_java_proto",
    deps = [":helloworld_proto"],
)

java_grpc_library(
    name = "helloworld_java_grpc",
    srcs = [":helloworld_proto"],
    deps = [":helloworld_java_proto"],
)
