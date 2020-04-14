load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

GRPC_VERSION = "1.28.1"

GRPC_JAVA_VERSION = "1.28.1"

REACTOR_GRPC_VERSION = "1.0.0"

def grpc_repositories():
    http_archive(
        name = "com_github_grpc_grpc",
        sha256 = "b0d3b876d85e4e4375aa211a52a33b7e8ca9f9d6d97a60c3c844070a700f0ea3",
        strip_prefix = "grpc-%s" % GRPC_VERSION,
        urls = ["https://github.com/grpc/grpc/archive/v%s.zip" % GRPC_VERSION],
    )

    http_archive(
        name = "io_grpc_grpc_java",
        sha256 = "c9ef39599b613a812843f1c43c90db9767f203b9a2ae6787f6bc715198e7dcb9",
        strip_prefix = "grpc-java-%s" % GRPC_JAVA_VERSION,
        urls = ["https://github.com/grpc/grpc-java/archive/v%s.zip" % GRPC_JAVA_VERSION],
    )

    http_archive(
        name = "com_salesforce_servicelibs_reactive_grpc",
        sha256 = "ecbd374daa9398634253d9bb2f58f0193217c1cd94d63b7274e87ad41e232ad4",
        strip_prefix = "reactive-grpc-%s" % REACTOR_GRPC_VERSION,
        urls = ["https://github.com/salesforce/reactive-grpc/archive/v%s.zip" % REACTOR_GRPC_VERSION],
    )

    http_archive(
        name = "io_grpc_grpc_proto",
        sha256 = "9d96f861f01ed9e3d805024e72a6b218b626da2114c69c1cad5d0e967c8e23be",
        strip_prefix = "grpc-proto-435d723289d348e1bc420d420b364369d565182a",
        urls = ["https://github.com/grpc/grpc-proto/archive/435d723289d348e1bc420d420b364369d565182a.zip"],
    )

IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS = {
    "io.grpc:grpc-api": "@com_cgi_eoss_eopp//third_party/grpc:api",
    "io.grpc:grpc-context": "@com_cgi_eoss_eopp//third_party/grpc:context",
    "io.grpc:grpc-core": "@com_cgi_eoss_eopp//third_party/grpc:all_core",
    "io.grpc:grpc-netty": "@com_cgi_eoss_eopp//third_party/grpc:netty",
    "io.grpc:grpc-protobuf": "@com_cgi_eoss_eopp//third_party/grpc:protobuf",
    "io.grpc:grpc-protobuf-lite": "@com_cgi_eoss_eopp//third_party/grpc:protobuf_lite",
    # TODO Override the bazel grpc-services target when they fix deprecation warnings (or we can suppress them)
    #"io.grpc:grpc-services": "@com_cgi_eoss_eopp//third_party/grpc:all_services",
    "io.grpc:grpc-stub": "@com_cgi_eoss_eopp//third_party/grpc:stub",
}
