load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

GRPC_JAVA_VERSION = "1.41.0"

REACTOR_GRPC_VERSION = "1.2.0"

def grpc_repositories():
    http_archive(
        name = "io_grpc_grpc_java",
        sha256 = "a61a678f995f1d612bb23d5fb721d83b6960508cc1e0b0dc3c164d6d8d8d24e0",
        strip_prefix = "grpc-java-%s" % GRPC_JAVA_VERSION,
        urls = ["https://github.com/grpc/grpc-java/archive/v%s.zip" % GRPC_JAVA_VERSION],
    )

    http_archive(
        name = "com_salesforce_servicelibs_reactive_grpc",
        sha256 = "d65989d5ecf8b739c653263e6dc1b06fbbf8fab11f8aeb6df4abf5e5de7abab8",
        strip_prefix = "reactive-grpc-%s" % REACTOR_GRPC_VERSION,
        urls = ["https://github.com/salesforce/reactive-grpc/archive/v%s.zip" % REACTOR_GRPC_VERSION],
    )

    # From https://github.com/grpc/grpc-java/blob/{GRPC_JAVA_VERSION}/repositories.bzl
    http_archive(
        name = "io_grpc_grpc_proto",
        sha256 = "464e97a24d7d784d9c94c25fa537ba24127af5aae3edd381007b5b98705a0518",
        strip_prefix = "grpc-proto-08911e9d585cbda3a55eb1dcc4b99c89aebccff8",
        urls = ["https://github.com/grpc/grpc-proto/archive/08911e9d585cbda3a55eb1dcc4b99c89aebccff8.zip"],
    )

IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS = {
    "io.grpc:grpc-alts": "@com_cgi_eoss_eopp//third_party/grpc:alts",
    "io.grpc:grpc-api": "@com_cgi_eoss_eopp//third_party/grpc:api",
    "io.grpc:grpc-auth": "@com_cgi_eoss_eopp//third_party/grpc:auth",
    "io.grpc:grpc-census": "@com_cgi_eoss_eopp//third_party/grpc:census",
    "io.grpc:grpc-context": "@com_cgi_eoss_eopp//third_party/grpc:context",
    "io.grpc:grpc-core": "@com_cgi_eoss_eopp//third_party/grpc:core",
    "io.grpc:grpc-grpclb": "@com_cgi_eoss_eopp//third_party/grpc:grpclb",
    "io.grpc:grpc-netty": "@com_cgi_eoss_eopp//third_party/grpc:netty",
    "io.grpc:grpc-protobuf": "@com_cgi_eoss_eopp//third_party/grpc:protobuf",
    "io.grpc:grpc-protobuf-lite": "@com_cgi_eoss_eopp//third_party/grpc:protobuf_lite",
    # TODO Override the bazel grpc-services target when they fix deprecation warnings (or we can suppress them)
    #"io.grpc:grpc-services": "@com_cgi_eoss_eopp//third_party/grpc:all_services",
    "io.grpc:grpc-stub": "@com_cgi_eoss_eopp//third_party/grpc:stub",
    "io.grpc:grpc-testing": "@com_cgi_eoss_eopp//third_party/grpc:testing",
}
