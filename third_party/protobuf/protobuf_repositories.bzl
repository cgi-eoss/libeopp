load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:utils.bzl", "maybe")

PROTOBUF_VERSION = "29.3"
PROTOBUF_JAVA_VERSION = "4.29.3"
PROTOBUF_SHA256 = "85803e01f347141e16a2f770213a496f808fff9f0138c7c0e0c9dfa708b0da92"

def protobuf_repositories():
    maybe(
        http_archive,
        name = "com_google_protobuf",
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
