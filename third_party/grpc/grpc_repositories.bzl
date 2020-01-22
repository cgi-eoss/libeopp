load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

GRPC_VERSION = "1.26.0"

GRPC_JAVA_VERSION = "1.26.0"

REACTOR_GRPC_VERSION = "1.0.0"

def grpc_repositories():
    http_archive(
        name = "com_github_grpc_grpc",
        sha256 = "b90b2328e8546065578cc7a2b354ba50461365e162d8745e539045b1ca040963",
        strip_prefix = "grpc-%s" % GRPC_VERSION,
        urls = ["https://github.com/grpc/grpc/archive/v%s.zip" % GRPC_VERSION],
    )

    http_archive(
        name = "io_grpc_grpc_java",
        sha256 = "11f2930cf31c964406e8a7e530272a263fbc39c5f8d21410b2b927b656f4d9be",
        strip_prefix = "grpc-java-%s" % GRPC_JAVA_VERSION,
        urls = ["https://github.com/grpc/grpc-java/archive/v%s.zip" % GRPC_JAVA_VERSION],
        patches = ["//third_party/grpc:grpc_java_fix_renamed_javalite_target.patch"],
        patch_args = ["-p1"],
    )

    http_archive(
        name = "com_salesforce_servicelibs_reactive_grpc",
        sha256 = "ecbd374daa9398634253d9bb2f58f0193217c1cd94d63b7274e87ad41e232ad4",
        strip_prefix = "reactive-grpc-%s" % REACTOR_GRPC_VERSION,
        urls = ["https://github.com/salesforce/reactive-grpc/archive/v%s.zip" % REACTOR_GRPC_VERSION],
    )
