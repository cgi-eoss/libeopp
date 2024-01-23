load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:utils.bzl", "maybe")
load("//third_party/protobuf:protobuf_repositories.bzl", "protobuf_repositories")
load("//third_party/grpc:grpc_repositories.bzl", "grpc_repositories")

def libeopp_dependencies():
    maybe(
        http_archive,
        name = "platforms",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/platforms/releases/download/0.0.8/platforms-0.0.8.tar.gz",
            "https://github.com/bazelbuild/platforms/releases/download/0.0.8/platforms-0.0.8.tar.gz",
        ],
        sha256 = "8150406605389ececb6da07cbcb509d5637a3ab9a24bc69b1101531367d89d74",
    )

    maybe(
        http_archive,
        name = "google_bazel_common",
        sha256 = "de1441c02b35f5768b872d15d7e5813c4826b66630703e253fc95da39988a6d8",
        strip_prefix = "bazel-common-d4ada735afa0ab044957cfa21849be577756a6cd",
        urls = ["https://github.com/google/bazel-common/archive/d4ada735afa0ab044957cfa21849be577756a6cd.tar.gz"],
    )

    maybe(
        http_archive,
        name = "bazel_skylib",
        sha256 = "cd55a062e763b9349921f0f5db8c3933288dc8ba4f76dd9416aac68acee3cb94",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.5.0/bazel-skylib-1.5.0.tar.gz",
            "https://github.com/bazelbuild/bazel-skylib/releases/download/1.5.0/bazel-skylib-1.5.0.tar.gz",
        ],
    )

    HERMETIC_CC_TOOLCHAIN_VERSION = "v2.2.1"
    HERMETIC_CC_TOOLCHAIN_SHA = "3b8107de0d017fe32e6434086a9568f97c60a111b49dc34fc7001e139c30fdea"

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
        sha256 = "d71d2c67e0bce986e1c5a7731b4693226867c45bfe0b7c5e0067228a536fc580",
        strip_prefix = "rules_python-0.29.0",
        url = "https://github.com/bazelbuild/rules_python/releases/download/0.29.0/rules_python-0.29.0.tar.gz",
    )

    maybe(
        http_archive,
        name = "rules_pkg",
        sha256 = "8f9ee2dc10c1ae514ee599a8b42ed99fa262b757058f65ad3c384289ff70c4b8",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/rules_pkg/releases/download/0.9.1/rules_pkg-0.9.1.tar.gz",
            "https://github.com/bazelbuild/rules_pkg/releases/download/0.9.1/rules_pkg-0.9.1.tar.gz",
        ],
    )

    maybe(
        http_archive,
        name = "rules_java",
        urls = [
            "https://github.com/bazelbuild/rules_java/releases/download/7.3.2/rules_java-7.3.2.tar.gz",
        ],
        sha256 = "3121a00588b1581bd7c1f9b550599629e5adcc11ba9c65f482bbd5cfe47fdf30",
    )

    maybe(
        http_archive,
        name = "rules_proto",
        sha256 = "903af49528dc37ad2adbb744b317da520f133bc1cbbecbdd2a6c546c9ead080b",
        strip_prefix = "rules_proto-6.0.0-rc0",
        url = "https://github.com/bazelbuild/rules_proto/releases/download/6.0.0-rc0/rules_proto-6.0.0-rc0.tar.gz",
    )

    RULES_JVM_EXTERNAL_TAG = "6.0"
    RULES_JVM_EXTERNAL_SHA = "85fd6bad58ac76cc3a27c8e051e4255ff9ccd8c92ba879670d195622e7c0a9b7"

    maybe(
        http_archive,
        name = "rules_jvm_external",
        strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
        sha256 = RULES_JVM_EXTERNAL_SHA,
        url = "https://github.com/bazelbuild/rules_jvm_external/releases/download/%s/rules_jvm_external-%s.tar.gz" % (RULES_JVM_EXTERNAL_TAG, RULES_JVM_EXTERNAL_TAG),
    )

    rules_kotlin_version = "v1.9.0"
    rules_kotlin_sha = "5766f1e599acf551aa56f49dab9ab9108269b03c557496c54acaf41f98e2b8d6"

    maybe(
        http_archive,
        name = "rules_kotlin",
        sha256 = rules_kotlin_sha,
        urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/%s/rules_kotlin-%s.tar.gz" % (rules_kotlin_version, rules_kotlin_version)],
    )

    maybe(
        http_archive,
        name = "bazel_sonarqube",
        sha256 = "5b2d6e2f83b9fb375dd422b2c6571ac223d89b6e1915f5d0b8e37aa04354ba32",
        strip_prefix = "bazel-sonarqube-d6109e6627a00ad84c24c38df5d2d17159203f47",
        urls = ["https://github.com/Zetten/bazel-sonarqube/archive/d6109e6627a00ad84c24c38df5d2d17159203f47.zip"],
    )
