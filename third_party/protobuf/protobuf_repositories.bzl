load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

PROTOBUF_VERSION = "3.10.0"

def protobuf_repositories():
    #    # Protobuf expects an //external:python_headers label which would contain the
    #    # Python headers if fast Python protos is enabled. Since we are not using fast
    #    # Python protos, bind python_headers to a dummy target.
    #    native.bind(
    #        name = "python_headers",
    #        actual = "//:dummy",
    #    )

    http_archive(
        name = "zlib",
        build_file = "@com_google_protobuf//:third_party/zlib.BUILD",
        sha256 = "c3e5e9fdd5004dcb542feda5ee4f0ff0744628baf8ed2dd5d66f8ca1197cb1a1",
        strip_prefix = "zlib-1.2.11",
        urls = ["https://zlib.net/zlib-1.2.11.tar.gz"],
    )

    http_archive(
        name = "com_google_protobuf",
        sha256 = "33cba8b89be6c81b1461f1c438424f7a1aa4e31998dbe9ed6f8319583daac8c7",
        strip_prefix = "protobuf-%s" % PROTOBUF_VERSION,
        urls = ["https://github.com/google/protobuf/archive/v%s.zip" % PROTOBUF_VERSION],
    )

    http_archive(
        name = "com_google_protobuf_javalite",
        sha256 = "a8cb9b8db16aff743a4bc8193abec96cf6ac0b0bc027121366b43ae8870f6fd3",
        strip_prefix = "protobuf-fa08222434bc58d743e8c2cc716bc219c3d0f44e",
        urls = ["https://github.com/google/protobuf/archive/fa08222434bc58d743e8c2cc716bc219c3d0f44e.zip"],
    )
