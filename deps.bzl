load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:utils.bzl", "maybe")
load("//third_party/grpc:grpc_repositories.bzl", "grpc_repositories")
load("//third_party/protobuf:protobuf_repositories.bzl", "protobuf_repositories")

def libeopp_dependencies():
    maybe(
        http_archive,
        name = "platforms",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/platforms/releases/download/0.0.10/platforms-0.0.10.tar.gz",
            "https://github.com/bazelbuild/platforms/releases/download/0.0.10/platforms-0.0.10.tar.gz",
        ],
        sha256 = "218efe8ee736d26a3572663b374a253c012b716d8af0c07e842e82f238a0a7ee",
    )

    maybe(
        http_archive,
        name = "google_bazel_common",
        sha256 = "32617f8f6ccce5d21bca9483674bd4a5d62f9d3daa6a68c309766c206214df6d",
        strip_prefix = "bazel-common-998614b6b1554428212b97e7174297a527fc6b90",
        urls = ["https://github.com/google/bazel-common/archive/998614b6b1554428212b97e7174297a527fc6b90.tar.gz"],
    )

    maybe(
        http_archive,
        name = "bazel_skylib",
        sha256 = "41449d7c7372d2e270e8504dfab09ee974325b0b40fdd98172c7fbe257b8bcc9",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.6.0/bazel-skylib-1.6.0.tar.gz",
            "https://github.com/bazelbuild/bazel-skylib/releases/download/1.6.0/bazel-skylib-1.6.0.tar.gz",
        ],
    )

    maybe(
        http_archive,
        name = "bazel_features",
        sha256 = "95fb3cfd11466b4cad6565e3647a76f89886d875556a4b827c021525cb2482bb",
        strip_prefix = "bazel_features-1.10.0",
        url = "https://github.com/bazel-contrib/bazel_features/releases/download/v1.10.0/bazel_features-v1.10.0.tar.gz",
    )

    maybe(
        http_archive,
        name = "aspect_bazel_lib",
        sha256 = "b554eb7942a5ab44c90077df6a0c76fc67c5874c9446a007e9ba68be82bd4796",
        strip_prefix = "bazel-lib-2.7.1",
        url = "https://github.com/aspect-build/bazel-lib/releases/download/v2.7.1/bazel-lib-v2.7.1.tar.gz",
    )

    HERMETIC_CC_TOOLCHAIN_VERSION = "v3.0.1"
    HERMETIC_CC_TOOLCHAIN_SHA = "3bc6ec127622fdceb4129cb06b6f7ab098c4d539124dde96a6318e7c32a53f7a"

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
        urls = ["https://github.com/bazelbuild/rules_cc/releases/download/0.0.9/rules_cc-0.0.9.tar.gz"],
        sha256 = "2037875b9a4456dce4a79d112a8ae885bbc4aad968e6587dca6e64f3a0900cdf",
        strip_prefix = "rules_cc-0.0.9",
    )

    maybe(
        http_archive,
        name = "rules_python",
        sha256 = "c68bdc4fbec25de5b5493b8819cfc877c4ea299c0dcb15c244c5a00208cde311",
        strip_prefix = "rules_python-0.31.0",
        url = "https://github.com/bazelbuild/rules_python/releases/download/0.31.0/rules_python-0.31.0.tar.gz",
    )

    maybe(
        http_archive,
        name = "rules_pkg",
        urls = [
            "https://github.com/bazelbuild/rules_pkg/releases/download/0.10.1/rules_pkg-0.10.1.tar.gz",
        ],
        sha256 = "d250924a2ecc5176808fc4c25d5cf5e9e79e6346d79d5ab1c493e289e722d1d0",
    )

    maybe(
        http_archive,
        name = "rules_java",
        urls = [
            "https://github.com/bazelbuild/rules_java/releases/download/7.5.0/rules_java-7.5.0.tar.gz",
        ],
        sha256 = "4da3761f6855ad916568e2bfe86213ba6d2637f56b8360538a7fb6125abf6518",
    )

    maybe(
        http_archive,
        name = "rules_proto",
        sha256 = "303e86e722a520f6f326a50b41cfc16b98fe6d1955ce46642a5b7a67c11c0f5d",
        strip_prefix = "rules_proto-6.0.0",
        url = "https://github.com/bazelbuild/rules_proto/releases/download/6.0.0/rules_proto-6.0.0.tar.gz",
    )

    RULES_JVM_EXTERNAL_TAG = "6.1"
    RULES_JVM_EXTERNAL_SHA = "08ea921df02ffe9924123b0686dc04fd0ff875710bfadb7ad42badb931b0fd50"

    maybe(
        http_archive,
        name = "rules_jvm_external",
        strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
        sha256 = RULES_JVM_EXTERNAL_SHA,
        url = "https://github.com/bazelbuild/rules_jvm_external/releases/download/%s/rules_jvm_external-%s.tar.gz" % (RULES_JVM_EXTERNAL_TAG, RULES_JVM_EXTERNAL_TAG),
    )

    maybe(
        http_archive,
        name = "rules_kotlin",
        sha256 = "34e8c0351764b71d78f76c8746e98063979ce08dcf1a91666f3f3bc2949a533d",
        url = "https://github.com/bazelbuild/rules_kotlin/releases/download/v1.9.5/rules_kotlin-v1.9.5.tar.gz",
    )

    maybe(
        http_archive,
        name = "bazel_sonarqube",
        sha256 = "5b2d6e2f83b9fb375dd422b2c6571ac223d89b6e1915f5d0b8e37aa04354ba32",
        strip_prefix = "bazel-sonarqube-d6109e6627a00ad84c24c38df5d2d17159203f47",
        urls = ["https://github.com/Zetten/bazel-sonarqube/archive/d6109e6627a00ad84c24c38df5d2d17159203f47.zip"],
    )
