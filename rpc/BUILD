load("@com_google_protobuf//bazel:java_proto_library.bzl", "java_proto_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")
load("@rules_proto//proto:defs.bzl", "proto_library")
load("//tools:maven.bzl", "maven_library")

maven_library(
    name = "rpc",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/rpc/GrpcClient.java",
        "src/main/java/com/cgi/eoss/eopp/rpc/GrpcMethod.java",
        "src/main/java/com/cgi/eoss/eopp/rpc/package-info.java",
    ],
    analysis_srcs = glob(["src/main/java/**/*.java"]),
    analysis_targets = [
        ":rpc",
        ":grpc-discovery-client-name-resolver",
    ],
    artifact_name = "libeopp :: rpc",
    root_packages = ["com.cgi.eoss.eopp.rpc"],
    test_suite = [":rpc_tests"],
    deps = [
        "//third_party/grpc:api",
        "//third_party/grpc:core_inprocess",
        "//third_party/grpc:stub",
        "//third_party/java:org_springframework_spring_core_neverlink",
        "//third_party/protobuf:protobuf_java",
        "//util",
        "@maven//:com_google_guava_guava",
        "@maven//:org_slf4j_slf4j_api",
    ],
)

maven_library(
    name = "grpc-discovery-client-name-resolver",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/rpc/DiscoveryClientNameResolver.java",
        "src/main/java/com/cgi/eoss/eopp/rpc/DiscoveryClientNameResolverProvider.java",
    ],
    artifact_name = "libeopp :: rpc :: grpc-discovery-client-name-resolver",
    generate_pitest_coverage_target = False,
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.rpc"],
    deps = [
        "//third_party/grpc:api",
        "@maven//:com_google_code_findbugs_jsr305",
        "@maven//:org_springframework_cloud_spring_cloud_commons",
        "@maven//:org_springframework_spring_beans",
        "@maven//:org_springframework_spring_core",
    ],
)

gen_java_tests(
    name = "rpc_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":grpc-discovery-client-name-resolver",
        ":helloworld_java_grpc",
        ":helloworld_java_proto",
        ":rpc",
        "@io_grpc_grpc_java//core",
        "@io_grpc_grpc_java//core:inprocess",
        "@io_grpc_grpc_java//netty",
        "@io_grpc_grpc_java//stub",
        "@io_grpc_grpc_java//testing",
        "@maven//:com_google_guava_guava",
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
