load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

PROTOBUF_VERSION = "21.1"
PROTOBUF_JAVA_VERSION = "3.21.1"
PROTOBUF_SHA256 = "6a2a9b6d00e9a5a28d251f4dc5f2a7e72684a699206ec286ea02ac0b02c22e77"

def protobuf_repositories():
    #    # Protobuf expects an //external:python_headers label which would contain the
    #    # Python headers if fast Python protos is enabled. Since we are not using fast
    #    # Python protos, bind python_headers to a dummy target.
    #    native.bind(
    #        name = "python_headers",
    #        actual = "//:dummy",
    #    )

    if not native.existing_rule("zlib"):
        http_archive(
            name = "zlib",
            build_file = Label("@com_google_protobuf//:third_party/zlib.BUILD"),
            sha256 = "629380c90a77b964d896ed37163f5c3a34f6e6d897311f1df2a7016355c45eff",
            strip_prefix = "zlib-1.2.11",
            urls = ["https://github.com/madler/zlib/archive/v1.2.11.tar.gz"],
        )

    http_archive(
        name = "com_google_protobuf",
        sha256 = PROTOBUF_SHA256,
        strip_prefix = "protobuf-%s" % PROTOBUF_VERSION,
        urls = ["https://github.com/protocolbuffers/protobuf/archive/v%s.zip" % PROTOBUF_VERSION],
    )

    http_archive(
        name = "com_google_protobuf_javalite",
        sha256 = PROTOBUF_SHA256,
        strip_prefix = "protobuf-%s" % PROTOBUF_VERSION,
        urls = ["https://github.com/protocolbuffers/protobuf/archive/v%s.zip" % PROTOBUF_VERSION],
    )

COM_GOOGLE_PROTOBUF_JAVA_OVERRIDE_TARGETS = {
    "com.google.protobuf:protobuf-java": "@com_cgi_eoss_eopp//third_party/protobuf:protobuf_java",
    "com.google.protobuf:protobuf-java-util": "@com_cgi_eoss_eopp//third_party/protobuf:protobuf_java_util",
    "com.google.protobuf:protobuf-javalite": "@com_cgi_eoss_eopp//third_party/protobuf:protobuf_javalite",
}
