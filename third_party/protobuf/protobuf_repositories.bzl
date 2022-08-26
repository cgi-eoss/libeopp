load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

PROTOBUF_VERSION = "21.9"
PROTOBUF_JAVA_VERSION = "3.21.9"
PROTOBUF_SHA256 = "5babb8571f1cceafe0c18e13ddb3be556e87e12ceea3463d6b0d0064e6cc1ac3"

def protobuf_repositories():
    if not native.existing_rule("zlib"):
        http_archive(
            name = "zlib",
            build_file = "@com_google_protobuf//:third_party/zlib.BUILD",
            sha256 = "d14c38e313afc35a9a8760dadf26042f51ea0f5d154b0630a31da0540107fb98",
            strip_prefix = "zlib-1.2.13",
            urls = [
                "https://github.com/madler/zlib/releases/download/v1.2.13/zlib-1.2.13.tar.xz",
                "https://zlib.net/zlib-1.2.13.tar.xz",
            ],
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
    # TODO Override the bazel protobuf-kotlin target when we can import all the additional non-visible targets
    #"com.google.protobuf:protobuf-kotlin": "@com_cgi_eoss_eopp//third_party/protobuf:protobuf_kotlin",
}
