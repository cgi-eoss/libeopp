load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

PROTOBUF_VERSION = "3.11.4"

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
        sha256 = "9748c0d90e54ea09e5e75fb7fac16edce15d2028d4356f32211cfa3c0e956564",
        strip_prefix = "protobuf-%s" % PROTOBUF_VERSION,
        urls = ["https://github.com/google/protobuf/archive/v%s.zip" % PROTOBUF_VERSION],
        patches = ["@com_cgi_eoss_eopp//third_party/protobuf:protobuf_drop_java_7_compatibility.patch"],
        patch_args = ["-p1"],
    )

    http_archive(
        name = "com_google_protobuf_javalite",
        sha256 = "e60211a40473f6be95b53f64559f82a3b2971672b11710db2fc9081708e25699",
        strip_prefix = "protobuf-0425fa932ce95a32bb9f88b2c09b995e9ff8207b",
        urls = ["https://github.com/google/protobuf/archive/0425fa932ce95a32bb9f88b2c09b995e9ff8207b.zip"],  # Commit with fixed javalite on 3.11.x branch
        patches = ["@com_cgi_eoss_eopp//third_party/protobuf:protobuf_drop_java_7_compatibility.patch"],
        patch_args = ["-p1"],
    )

COM_GOOGLE_PROTOBUF_JAVA_OVERRIDE_TARGETS = {
    "com.google.protobuf:protobuf-java": "@com_cgi_eoss_eopp//third_party/protobuf:protobuf_java",
    "com.google.protobuf:protobuf-java-util": "@com_cgi_eoss_eopp//third_party/protobuf:protobuf_java_util",
    "com.google.protobuf:protobuf-javalite": "@com_cgi_eoss_eopp//third_party/protobuf:protobuf_javalite",
}
