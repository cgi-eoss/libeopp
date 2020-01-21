load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

PROTOBUF_VERSION = "3.11.1"

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
        sha256 = "20e55e7dc9ebbb5800072fff25fd56d7c0a168493ef4652e78910566fa6b45f5",
        strip_prefix = "protobuf-%s" % PROTOBUF_VERSION,
        urls = ["https://github.com/google/protobuf/archive/v%s.zip" % PROTOBUF_VERSION],
        patches = ["//third_party/protobuf:protobuf_drop_java_7_compatibility.patch"],
        patch_args = ["-p1"],
    )

    http_archive(
        name = "com_google_protobuf_javalite",
        sha256 = "311b29b8d0803ab4f89be22ff365266abb6c48fd3483d59b04772a144d7a24a1",
        strip_prefix = "protobuf-7b64714af67aa967dcf941df61fe5207975966be",
        urls = ["https://github.com/google/protobuf/archive/7b64714af67aa967dcf941df61fe5207975966be.zip"],
    )
