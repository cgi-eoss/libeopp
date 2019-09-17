load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def grpc_repositories():
    http_archive(
        name = "com_github_grpc_grpc",
        sha256 = "86d7552cb79ab9ba7243d86b768952df1907bacb828f5f53b8a740f716f3937b",
        strip_prefix = "grpc-1.23.0",
        urls = ["https://github.com/grpc/grpc/archive/v1.23.0.zip"],
    )

    http_archive(
        name = "io_grpc_grpc_java",
        sha256 = "b1dcce395bdb6c620d3142597b5017f7175c527b0f9ae46c456726940876347e",
        strip_prefix = "grpc-java-1.23.0",
        urls = ["https://github.com/grpc/grpc-java/archive/v1.23.0.zip"],
    )

    http_archive(
        name = "com_salesforce_servicelibs_reactive_grpc",
        sha256 = "ecbd374daa9398634253d9bb2f58f0193217c1cd94d63b7274e87ad41e232ad4",
        strip_prefix = "reactive-grpc-1.0.0",
        urls = ["https://github.com/salesforce/reactive-grpc/archive/v1.0.0.zip"],
    )
