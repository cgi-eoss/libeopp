filegroup(
    name = "rpc_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

filegroup(
    name = "rpc_java_resources",
    srcs = glob(["src/main/resources/**"]),
)

java_library(
    name = "rpc",
    srcs = [":rpc_java_srcs"],
    visibility = ["//visibility:public"],
    deps = [
        "//util",
        "@com_google_code_findbugs_jsr305",
        "@io_grpc_grpc_java//core",
        "@io_grpc_grpc_java//core:inprocess",
        "@org_slf4j_slf4j_api",
        "@org_springframework_cloud_spring_cloud_commons",
        "@org_springframework_spring_core",
    ],
)

java_library(
    name = "rpc_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":helloworld_java_grpc",
        ":helloworld_java_proto",
        ":rpc",
        "@com_google_truth_truth",
        "@io_grpc_grpc_java//core",
        "@io_grpc_grpc_java//core:inprocess",
        "@io_grpc_grpc_java//stub",
        "@io_grpc_grpc_java//testing",
        "@junit_junit",
        "@org_mockito_mockito_core",
    ],
)

java_test(
    name = "GrpcClientTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.rpc.GrpcClientTest",
    runtime_deps = [":rpc_java_tests"],
)

load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")

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
