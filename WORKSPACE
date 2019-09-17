workspace(name = "com_cgi_eoss_eopp")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "google_bazel_common",
    sha256 = "2f4ff4f09df4143cba49ca2c3750c1e8f544cf5a4a6164d37b6dc59d224d0c12",
    strip_prefix = "bazel-common-3ce0644a7d7da09218b96b3218f409ea8f8c84e6",
    urls = ["https://github.com/google/bazel-common/archive/3ce0644a7d7da09218b96b3218f409ea8f8c84e6.zip"],
)

http_archive(
    name = "bazel_skylib",
    sha256 = "2e351c3b4861b0c5de8db86fdd100869b544c759161008cd93949dddcbfaba53",
    strip_prefix = "bazel-skylib-0.8.0",
    urls = ["https://github.com/bazelbuild/bazel-skylib/archive/0.8.0.zip"],
)

http_archive(
    name = "rules_cc",
    sha256 = "35f2fb4ea0b3e61ad64a369de284e4fbbdcdba71836a5555abb5e194cf119509",
    strip_prefix = "rules_cc-624b5d59dfb45672d4239422fa1e3de1822ee110",
    urls = ["https://github.com/bazelbuild/rules_cc/archive/624b5d59dfb45672d4239422fa1e3de1822ee110.tar.gz"],
)

http_archive(
    name = "rules_java",
    sha256 = "ccf00372878d141f7d5568cedc4c42ad4811ba367ea3e26bc7c43445bbc52895",
    strip_prefix = "rules_java-d7bf804c8731edd232cb061cb2a9fe003a85d8ee",
    urls = ["https://github.com/bazelbuild/rules_java/archive/d7bf804c8731edd232cb061cb2a9fe003a85d8ee.tar.gz"],
)

http_archive(
    name = "rules_proto",
    sha256 = "57001a3b33ec690a175cdf0698243431ef27233017b9bed23f96d44b9c98242f",
    strip_prefix = "rules_proto-9cd4f8f1ede19d81c6d48910429fe96776e567b1",
    urls = ["https://github.com/bazelbuild/rules_proto/archive/9cd4f8f1ede19d81c6d48910429fe96776e567b1.tar.gz"],
)

http_archive(
    name = "rules_python",
    sha256 = "b5bab4c47e863e0fbb77df4a40c45ca85f98f5a2826939181585644c9f31b97b",
    strip_prefix = "rules_python-9d68f24659e8ce8b736590ba1e4418af06ec2552",
    urls = ["https://github.com/bazelbuild/rules_python/archive/9d68f24659e8ce8b736590ba1e4418af06ec2552.tar.gz"],
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
    omit_io_grpc_grpc_services = False,  # TODO Mask when grpc-java fix the deprecated imports
    omit_io_grpc_grpc_stub = True,
    replacements = {
        "@com_google_guava_listenablefuture": [],  # Empty jar anyway to avoid guava conflict
        "@com_google_protobuf_protobuf_java": ["@com_google_protobuf//:protobuf_java"],
        "@com_google_protobuf_protobuf_java_util": ["@com_google_protobuf//:protobuf_java_util"],
        "@io_grpc_grpc_context": ["@io_grpc_grpc_java//context"],
        "@io_grpc_grpc_core": [
            # The published jar contains all these targets
            "@io_grpc_grpc_java//core",
            "@io_grpc_grpc_java//core:inprocess",
            "@io_grpc_grpc_java//core:util",
        ],
        "@io_grpc_grpc_netty": ["@io_grpc_grpc_java//netty"],
        "@io_grpc_grpc_protobuf": ["@io_grpc_grpc_java//protobuf"],
        "@io_grpc_grpc_protobuf_lite": ["@io_grpc_grpc_java//protobuf-lite"],
        "@io_grpc_grpc_stub": ["@io_grpc_grpc_java//stub"],
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
