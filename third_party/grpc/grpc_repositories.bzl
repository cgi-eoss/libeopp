load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

GRPC_VERSION = "1.25.0"

GRPC_JAVA_VERSION = "1.25.0"

def grpc_repositories():
    http_archive(
        name = "com_github_grpc_grpc",
        sha256 = "82de7c3754df7be44c65fd00decd5e2351ea64e4d21fdaf6d76cea5676f954e8",
        strip_prefix = "grpc-%s" % GRPC_VERSION,
        urls = ["https://github.com/grpc/grpc/archive/v%s.zip" % GRPC_VERSION],
    )

    http_archive(
        name = "io_grpc_grpc_java",
        sha256 = "e44ebf25c8885233b15d522e3349c9b495945adea5f3e1d5a287a9ec437ecec5",
        strip_prefix = "grpc-java-%s" % GRPC_JAVA_VERSION,
        urls = ["https://github.com/grpc/grpc-java/archive/v%s.zip" % GRPC_JAVA_VERSION],
    )

    http_archive(
        name = "com_salesforce_servicelibs_reactive_grpc",
        sha256 = "ecbd374daa9398634253d9bb2f58f0193217c1cd94d63b7274e87ad41e232ad4",
        strip_prefix = "reactive-grpc-1.0.0",
        urls = ["https://github.com/salesforce/reactive-grpc/archive/v1.0.0.zip"],
    )
