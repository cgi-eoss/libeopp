load("//tools:maven.bzl", "maven_coordinates_tag", "maven_library")
load("@com_github_grpc_grpc_kotlin//:kt_jvm_grpc.bzl", "kt_jvm_proto_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@google_bazel_common//tools/jarjar:jarjar.bzl", "jarjar_library")
load("@rules_java//java:defs.bzl", "java_proto_library")
load("@rules_proto//proto:defs.bzl", "proto_library")

filegroup(
    name = "identifier_proto_srcs",
    srcs = glob(["src/main/proto/**/*.proto"]),
)

filegroup(
    name = "identifier_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

proto_library(
    name = "identifier_proto",
    srcs = [":identifier_proto_srcs"],
    visibility = ["//visibility:public"],
)

java_proto_library(
    name = "identifier_java_proto",
    visibility = ["//visibility:public"],
    deps = [":identifier_proto"],
)

kt_jvm_proto_library(
    name = "identifier_kt_jvm_proto",
    java_proto_target = "identifier_java_proto",
    visibility = ["//visibility:public"],
    deps = [":identifier_proto"],
)

maven_library(
    name = "identifier",
    srcs = [":identifier_java_srcs"],
    artifact_name = "libeopp :: identifier",
    deploy_java_library = False,
    generate_pitest_coverage_target = True,
    root_packages = ["com.cgi.eoss.eopp.identifier"],
    test_suite = [":identifier_tests"],
    exports = [
        ":identifier_java_proto",
        ":identifier_kt_jvm_proto",
    ],
    deps = [
        ":identifier_java_proto",
        "//third_party/java:org_springframework_spring_core_neverlink",
        "//third_party/protobuf:protobuf_java",
    ],
)

# Bundle the proto-derived classes for the Maven artifact
jarjar_library(
    name = "identifier_deploy",
    jars = [
        ":identifier",
        ":identifier_java_proto",
        ":identifier_kt_jvm_proto.jar",
    ],
    tags = [
        "maven_artifact",
        maven_coordinates_tag("identifier"),
    ],
    visibility = ["//:__pkg__"],
)

gen_java_tests(
    name = "identifier_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":identifier",
        ":identifier_test_java_proto",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)

proto_library(
    name = "identifier_test_proto",
    srcs = ["src/test/proto/identifier_test_server.proto"],
    visibility = ["//visibility:public"],
)

java_proto_library(
    name = "identifier_test_java_proto",
    visibility = ["//visibility:public"],
    deps = [":identifier_test_proto"],
)
