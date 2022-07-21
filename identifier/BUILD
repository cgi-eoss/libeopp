load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")

maven_library(
    name = "identifier",
    artifact_name = "libeopp :: identifier",
    protos = [{
        "name": "identifier",
        "srcs": "src/main/proto/identifier.proto",
    }],
    root_packages = ["com.cgi.eoss.eopp.identifier"],
    test_suite = [":identifier_tests"],
    deps = [
        "//third_party/java:org_springframework_spring_core_neverlink",
        "@maven//:com_google_protobuf_protobuf_java",
    ],
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
