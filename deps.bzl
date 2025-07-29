load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:utils.bzl", "maybe")
load("//third_party/grpc:grpc_repositories.bzl", "grpc_repositories")
load("//third_party/protobuf:protobuf_repositories.bzl", "protobuf_repositories")

def libeopp_dependencies():
    maybe(
        http_archive,
        name = "platforms",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/platforms/releases/download/1.0.0/platforms-1.0.0.tar.gz",
            "https://github.com/bazelbuild/platforms/releases/download/1.0.0/platforms-1.0.0.tar.gz",
        ],
        sha256 = "3384eb1c30762704fbe38e440204e114154086c8fc8a8c2e3e28441028c019a8",
    )

    maybe(
        http_archive,
        name = "google_bazel_common",
        sha256 = "03c5fd4ba66e8b72650a3533879d0420e28716828ac79d0c8dc02a1010899236",
        strip_prefix = "bazel-common-8c50e017875b3fa93729cb75d68c345c4ef8b5d1",
        urls = ["https://github.com/google/bazel-common/archive/8c50e017875b3fa93729cb75d68c345c4ef8b5d1.tar.gz"],
    )

    maybe(
        http_archive,
        name = "bazel_skylib",
        sha256 = "51b5105a760b353773f904d2bbc5e664d0987fbaf22265164de65d43e910d8ac",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.8.1/bazel-skylib-1.8.1.tar.gz",
            "https://github.com/bazelbuild/bazel-skylib/releases/download/1.8.1/bazel-skylib-1.8.1.tar.gz",
        ],
    )

    maybe(
        http_archive,
        name = "bazel_features",
        sha256 = "07bd2b18764cdee1e0d6ff42c9c0a6111ffcbd0c17f0de38e7f44f1519d1c0cd",
        strip_prefix = "bazel_features-1.32.0",
        url = "https://github.com/bazel-contrib/bazel_features/releases/download/v1.32.0/bazel_features-v1.32.0.tar.gz",
    )

    maybe(
        http_archive,
        name = "rules_shell",
        sha256 = "b15cc2e698a3c553d773ff4af35eb4b3ce2983c319163707dddd9e70faaa062d",
        strip_prefix = "rules_shell-0.5.0",
        url = "https://github.com/bazelbuild/rules_shell/releases/download/v0.5.0/rules_shell-v0.5.0.tar.gz",
    )

    maybe(
        http_archive,
        name = "aspect_bazel_lib",
        sha256 = "3522895fa13b97e8b27e3b642045682aa4233ae1a6b278aad6a3b483501dc9f2",
        strip_prefix = "bazel-lib-2.20.0",
        url = "https://github.com/bazel-contrib/bazel-lib/releases/download/v2.20.0/bazel-lib-v2.20.0.tar.gz",
    )

    HERMETIC_CC_TOOLCHAIN_VERSION = "v4.0.1"
    HERMETIC_CC_TOOLCHAIN_SHA = "364655885e5af5acd02b299e4eaf113a1317dd656253249b37b7c9f05bf23b79"

    maybe(
        http_archive,
        name = "hermetic_cc_toolchain",
        sha256 = HERMETIC_CC_TOOLCHAIN_SHA,
        urls = [
            "https://mirror.bazel.build/github.com/uber/hermetic_cc_toolchain/releases/download/{0}/hermetic_cc_toolchain-{0}.tar.gz".format(HERMETIC_CC_TOOLCHAIN_VERSION),
            "https://github.com/uber/hermetic_cc_toolchain/releases/download/{0}/hermetic_cc_toolchain-{0}.tar.gz".format(HERMETIC_CC_TOOLCHAIN_VERSION),
        ],
    )

    protobuf_repositories()

    grpc_repositories()

    maybe(
        http_archive,
        name = "rules_cc",
        sha256 = "0d3b4f984c4c2e1acfd1378e0148d35caf2ef1d9eb95b688f8e19ce0c41bdf5b",
        strip_prefix = "rules_cc-0.1.4",
        url = "https://github.com/bazelbuild/rules_cc/releases/download/0.1.4/rules_cc-0.1.4.tar.gz",
    )

    maybe(
        http_archive,
        name = "rules_python",
        sha256 = "fa532d635f29c038a64c8062724af700c30cf6b31174dd4fac120bc561a1a560",
        strip_prefix = "rules_python-1.5.1",
        url = "https://github.com/bazel-contrib/rules_python/releases/download/1.5.1/rules_python-1.5.1.tar.gz",
    )

    maybe(
        http_archive,
        name = "rules_pkg",
        urls = [
            "https://github.com/bazelbuild/rules_pkg/releases/download/1.1.0/rules_pkg-1.1.0.tar.gz",
        ],
        sha256 = "b7215c636f22c1849f1c3142c72f4b954bb12bb8dcf3cbe229ae6e69cc6479db",
    )

    maybe(
        http_archive,
        name = "rules_java",
        urls = [
            "https://github.com/bazelbuild/rules_java/releases/download/8.14.0/rules_java-8.14.0.tar.gz",
        ],
        sha256 = "bbe7d94360cc9ed4607ec5fd94995fd1ec41e84257020b6f09e64055281ecb12",
    )

    RULES_JVM_EXTERNAL_TAG = "6.6"
    RULES_JVM_EXTERNAL_SHA = "3afe5195069bd379373528899c03a3072f568d33bd96fe037bd43b1f590535e7"

    maybe(
        http_archive,
        name = "rules_jvm_external",
        strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
        sha256 = RULES_JVM_EXTERNAL_SHA,
        url = "https://github.com/bazel-contrib/rules_jvm_external/releases/download/%s/rules_jvm_external-%s.tar.gz" % (RULES_JVM_EXTERNAL_TAG, RULES_JVM_EXTERNAL_TAG),
    )

    maybe(
        http_archive,
        name = "rules_kotlin",
        sha256 = "e6ca53740703a709a017bf4441f260007a6075741af65da1b79bc31ada9a570a",
        url = "https://github.com/bazelbuild/rules_kotlin/releases/download/v2.1.8/rules_kotlin-v2.1.8.tar.gz",
    )

    maybe(
        http_archive,
        name = "rules_license",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/rules_license/releases/download/1.0.0/rules_license-1.0.0.tar.gz",
            "https://github.com/bazelbuild/rules_license/releases/download/1.0.0/rules_license-1.0.0.tar.gz",
        ],
        sha256 = "26d4021f6898e23b82ef953078389dd49ac2b5618ac564ade4ef87cced147b38",
    )

    maybe(
        http_archive,
        name = "bazel_sonarqube",
        sha256 = "d8bc5213fabb775301393e4e5b05690f822470afeb8472b696dd1c1b91c8e558",
        url = "https://github.com/Zetten/bazel-sonarqube/releases/download/1.0.3/bazel_sonarqube.1.0.3.tar.gz",
    )
