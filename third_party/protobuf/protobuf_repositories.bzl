load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

PROTOBUF_VERSION = "3.12.2"

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
        sha256 = "b0a1da830747a2ffc1125fc84dbd3fe32a876396592d4580501749a2d0d0cb15",
        strip_prefix = "protobuf-%s" % PROTOBUF_VERSION,
        patches = ["@com_cgi_eoss_eopp//third_party/protobuf:protobuf_drop_java_7_compatibility.patch"],
        patch_args = ["-p1"],
        urls = ["https://github.com/google/protobuf/archive/v%s.zip" % PROTOBUF_VERSION],
    )

    http_archive(
        name = "com_google_protobuf_javalite",
        sha256 = "b0a1da830747a2ffc1125fc84dbd3fe32a876396592d4580501749a2d0d0cb15",
        strip_prefix = "protobuf-%s" % PROTOBUF_VERSION,
        patches = ["@com_cgi_eoss_eopp//third_party/protobuf:protobuf_drop_java_7_compatibility.patch"],
        patch_args = ["-p1"],
        urls = ["https://github.com/google/protobuf/archive/v%s.zip" % PROTOBUF_VERSION],
    )

COM_GOOGLE_PROTOBUF_JAVA_OVERRIDE_TARGETS = {
    "com.google.protobuf:protobuf-java": "@com_cgi_eoss_eopp//third_party/protobuf:protobuf_java",
    "com.google.protobuf:protobuf-java-util": "@com_cgi_eoss_eopp//third_party/protobuf:protobuf_java_util",
    "com.google.protobuf:protobuf-javalite": "@com_cgi_eoss_eopp//third_party/protobuf:protobuf_javalite",
}
