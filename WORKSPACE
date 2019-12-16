workspace(name = "com_cgi_eoss_eopp")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "google_bazel_common",
    sha256 = "090d1f394c2bbeae37f091a9d7853bafc7a9b3174d1e100d762fdd07767a2269",
    strip_prefix = "bazel-common-1c8dcb31eed0713306cb6dc07f8334d84c925a01",
    urls = ["https://github.com/google/bazel-common/archive/1c8dcb31eed0713306cb6dc07f8334d84c925a01.zip"],
)

http_archive(
    name = "bazel_skylib",
    sha256 = "97e70364e9249702246c0e9444bccdc4b847bed1eb03c5a3ece4f83dfe6abc44",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.0.2/bazel-skylib-1.0.2.tar.gz",
        "https://github.com/bazelbuild/bazel-skylib/releases/download/1.0.2/bazel-skylib-1.0.2.tar.gz",
    ],
)

load("@bazel_skylib//:workspace.bzl", "bazel_skylib_workspace")

bazel_skylib_workspace()

http_archive(
    name = "rules_cc",
    sha256 = "dafda2ff2a913028ce1718253b6b2f353b2d2163470f3069ca810a0d8d55a5a9",
    strip_prefix = "rules_cc-cd7e8a690caf526e0634e3ca55b10308ee23182d",
    urls = ["https://github.com/bazelbuild/rules_cc/archive/cd7e8a690caf526e0634e3ca55b10308ee23182d.tar.gz"],
)

http_archive(
    name = "rules_java",
    sha256 = "1969a89e8da396eb7754fd0247b7df39b6df433c3dcca0095b4ba30a5409cc9d",
    strip_prefix = "rules_java-32ddd6c4f0ad38a54169d049ec05febc393b58fc",
    urls = ["https://github.com/bazelbuild/rules_java/archive/32ddd6c4f0ad38a54169d049ec05febc393b58fc.tar.gz"],
)

http_archive(
    name = "rules_proto",
    sha256 = "73ebe9d15ba42401c785f9d0aeebccd73bd80bf6b8ac78f74996d31f2c0ad7a6",
    strip_prefix = "rules_proto-2c0468366367d7ed97a1f702f9cd7155ab3f73c5",
    urls = ["https://github.com/bazelbuild/rules_proto/archive/2c0468366367d7ed97a1f702f9cd7155ab3f73c5.tar.gz"],
)

http_archive(
    name = "rules_python",
    sha256 = "c911dc70f62f507f3a361cbc21d6e0d502b91254382255309bc60b7a0f48de28",
    strip_prefix = "rules_python-38f86fb55b698c51e8510c807489c9f4e047480e",
    urls = ["https://github.com/bazelbuild/rules_python/archive/38f86fb55b698c51e8510c807489c9f4e047480e.tar.gz"],
)

load("//third_party/java:jarjar_repositories.bzl", "jarjar_repositories")

jarjar_repositories()

load("//third_party/java:java_repositories.bzl", "java_repositories")

java_repositories(
    fetch_sources = True,
    omit_com_google_guava_listenablefuture = True,
    omit_com_google_protobuf_protobuf_java = True,
    omit_com_google_protobuf_protobuf_java_util = True,
    omit_io_grpc_grpc_context = True,
    omit_io_grpc_grpc_core = True,
    omit_io_grpc_grpc_netty = True,
    omit_io_grpc_grpc_protobuf = True,
    omit_io_grpc_grpc_protobuf_lite = True,
    omit_io_grpc_grpc_services = True,  # TODO Mask when grpc-java fix the deprecated imports
    omit_io_grpc_grpc_stub = True,
    replacements = {
        "@com_google_guava_listenablefuture": [],  # Empty jar anyway to avoid guava conflict
        "@com_google_protobuf_protobuf_java": ["@com_cgi_eoss_eopp//third_party/protobuf:protobuf_java"],
        "@com_google_protobuf_protobuf_java_util": ["@com_cgi_eoss_eopp//third_party/protobuf:protobuf_java_util"],
        "@io_grpc_grpc_context": ["@com_cgi_eoss_eopp//third_party/grpc:context"],
        "@io_grpc_grpc_core": [
            "@com_cgi_eoss_eopp//third_party/grpc:core",
            "@com_cgi_eoss_eopp//third_party/grpc:core_inprocess",
            "@com_cgi_eoss_eopp//third_party/grpc:core_util",
        ],
        "@io_grpc_grpc_netty": ["@com_cgi_eoss_eopp//third_party/grpc:netty"],
        "@io_grpc_grpc_protobuf": ["@com_cgi_eoss_eopp//third_party/grpc:protobuf"],
        "@io_grpc_grpc_protobuf_lite": ["@com_cgi_eoss_eopp//third_party/grpc:protobuf_lite"],
        "@io_grpc_grpc_services": [
            "@com_cgi_eoss_eopp//third_party/grpc:services_binarylog",
            "@com_cgi_eoss_eopp//third_party/grpc:services_channelz",
            "@com_cgi_eoss_eopp//third_party/grpc:services_reflection",
        ],
        "@io_grpc_grpc_stub": ["@com_cgi_eoss_eopp//third_party/grpc:stub"],
    },
)

http_archive(
    name = "io_bazel_rules_kotlin",
    sha256 = "901db101944f0b10c655046e364adfa6eee86aec998651be6df50c77f5517ea1",
    strip_prefix = "rules_kotlin-2d822a3aef80f4934fe95d6ceadc6fa7cffff5eb",
    urls = ["https://github.com/cgruber/rules_kotlin/archive/2d822a3aef80f4934fe95d6ceadc6fa7cffff5eb.zip"],
)

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")

KOTLIN_VERSION = "1.3.50"

KOTLINC_RELEASE_SHA = "69424091a6b7f52d93eed8bba2ace921b02b113dbb71388d704f8180a6bdc6ec"

KOTLINC_RELEASE = {
    "urls": [
        "https://github.com/JetBrains/kotlin/releases/download/v{v}/kotlin-compiler-{v}.zip".format(v = KOTLIN_VERSION),
    ],
    "sha256": KOTLINC_RELEASE_SHA,
}

kotlin_repositories(compiler_release = KOTLINC_RELEASE)

kt_register_toolchains()

load("//third_party/protobuf:protobuf_repositories.bzl", "protobuf_repositories")

protobuf_repositories()

load("//third_party/grpc:grpc_repositories.bzl", "grpc_repositories")

grpc_repositories()

load("//third_party/grpc:grpc_dependency_repositories.bzl", "grpc_dependency_repositories")

grpc_dependency_repositories()

http_archive(
    name = "bazel_sonarqube",
    sha256 = "336b9b9953257d927ba74ff9423f21b4d406be48dfcb7dc9ab21adedbc91709d",
    strip_prefix = "bazel-sonarqube-7b84f80f5d852cab94e8148cbf2255136078e466",
    urls = ["https://github.com/Zetten/bazel-sonarqube/archive/7b84f80f5d852cab94e8148cbf2255136078e466.zip"],
)

load("@bazel_sonarqube//:repositories.bzl", "bazel_sonarqube_repositories")

bazel_sonarqube_repositories()
