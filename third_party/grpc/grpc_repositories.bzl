load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:utils.bzl", "maybe")

GRPC_VERSION = "1.65.1"
GRPC_JAVA_VERSION = "1.65.1"
GRPC_KOTLIN_VERSION = "1.4.1"
REACTOR_GRPC_VERSION = "1.2.4"

def grpc_repositories():
    maybe(
        http_archive,
        name = "com_github_grpc_grpc",
        sha256 = "0499a344a56cbd961836a1821b4c42f37d6d5698b373c5324492b01bb0377d25",
        strip_prefix = "grpc-%s" % GRPC_VERSION,
        urls = ["https://github.com/grpc/grpc/archive/v%s.zip" % GRPC_VERSION],
    )
    maybe(
        http_archive,
        name = "io_grpc_grpc_java",
        sha256 = "109c9f05068d8e01e4d371cb704d47515b0ececd12867dc065d774fadc328bfb",
        strip_prefix = "grpc-java-%s" % GRPC_JAVA_VERSION,
        urls = ["https://github.com/grpc/grpc-java/archive/v%s.zip" % GRPC_JAVA_VERSION],
    )

    maybe(
        http_archive,
        name = "com_salesforce_servicelibs_reactive_grpc",
        patch_args = ["-p1"],
        patches = ["@com_cgi_eoss_eopp//third_party/grpc:com_salesforce_servicelibs_reactive_grpc.patch"],
        sha256 = "e35e17d6275f62e88d10206f6d64c0452f1e2d376f828620f9a9bfc8334f9795",
        strip_prefix = "reactive-grpc-%s" % REACTOR_GRPC_VERSION,
        urls = ["https://github.com/salesforce/reactive-grpc/archive/v%s.zip" % REACTOR_GRPC_VERSION],
    )

    maybe(
        http_archive,
        name = "com_github_grpc_grpc_kotlin",
        patch_args = ["-p1"],
        patches = ["@com_cgi_eoss_eopp//third_party/grpc:com_github_grpc_grpc_kotlin.patch"],
        sha256 = "b576019f9222f47eef42258e5d964c04d87a01532c0df1a40a8f9fa1acc301c8",
        strip_prefix = "grpc-kotlin-%s" % GRPC_KOTLIN_VERSION,
        url = "https://github.com/grpc/grpc-kotlin/archive/v%s.zip" % GRPC_KOTLIN_VERSION,
    )

    # From https://github.com/grpc/grpc-java/blob/{GRPC_JAVA_VERSION}/repositories.bzl
    maybe(
        http_archive,
        name = "io_grpc_grpc_proto",
        sha256 = "729ac127a003836d539ed9da72a21e094aac4c4609e0481d6fc9e28a844e11af",
        strip_prefix = "grpc-proto-4f245d272a28a680606c0739753506880cf33b5f",
        urls = ["https://github.com/grpc/grpc-proto/archive/4f245d272a28a680606c0739753506880cf33b5f.zip"],
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
    "io.grpc:grpc-netty-shaded": "@com_cgi_eoss_eopp//third_party/grpc:netty-shaded",
    "io.grpc:grpc-protobuf": "@com_cgi_eoss_eopp//third_party/grpc:protobuf",
    "io.grpc:grpc-protobuf-lite": "@com_cgi_eoss_eopp//third_party/grpc:protobuf_lite",
    # TODO Override the bazel grpc-services target when they fix deprecation warnings (or we can suppress them)
    #"io.grpc:grpc-services": "@com_cgi_eoss_eopp//third_party/grpc:all_services",
    "io.grpc:grpc-stub": "@com_cgi_eoss_eopp//third_party/grpc:stub",
    "io.grpc:grpc-testing": "@com_cgi_eoss_eopp//third_party/grpc:testing",
}

IO_GRPC_GRPC_KOTLIN_OVERRIDE_TARGETS = {
    "io.grpc:grpc-kotlin-stub": "@com_cgi_eoss_eopp//third_party/grpc:kotlin_stub",
}
